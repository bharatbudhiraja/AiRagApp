import os
from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
from openai import OpenAI
from dotenv import load_dotenv
from sqlalchemy import create_engine, text
import uuid
from pydantic import BaseModel


# Load env variables
load_dotenv()

engine = create_engine(os.getenv("DATABASE_URL"))

app = FastAPI()

client = OpenAI(
    api_key=os.getenv("GITHUB_TOKEN"),
    base_url="https://models.inference.ai.azure.com"
)

# ✅ NEW MODEL (matches Spring Boot)
class Message(BaseModel):
    role: str
    content: str

class ChatRequest(BaseModel):
    messages: List[Message]


@app.post("/chat")
def chat(request: ChatRequest):

    # Step 1: Get latest user message
    user_query = request.messages[-1].content

    # Step 2: Fetch relevant context from DB
    context_docs = search_similar(user_query)
    context_text = "\n".join(context_docs)

    # Step 3: Updated system prompt with RAG
    messages = [
    {
        "role": "system",
        "content": f"""
You are an expert Android developer.

Use the following context if relevant:

{context_text}

If the context is not useful, answer normally.
"""
    }
]

    # ✅ Step 4: Add full conversation (same as before)
    for msg in request.messages:
        messages.append({
            "role": msg.role.lower(),
            "content": msg.content
        })

    # ✅ Step 5: Call LLM
    response = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=messages
    )

    return {
        "reply": response.choices[0].message.content
    }

@app.get("/test-db")
def test_db():
    with engine.connect() as conn:
        result = conn.execute(text("SELECT 1"))
        return {"result": str(result.fetchone())}

def get_embeddings(texts: list[str]):
    response = client.embeddings.create(
        model="text-embedding-3-small",
        input=texts
    )
    return [item.embedding for item in response.data]

def format_embedding(embedding: list[float]) -> str:
    return "[" + ",".join(map(str, embedding)) + "]"

class StoreRequest(BaseModel):
    content: str

@app.post("/store")
def store_text(request: StoreRequest):
    print("API HIT")

    content = request.content
    print("Content received")

    chunks = chunk_text(content)
    print("Chunks created:", len(chunks))

    embeddings = get_embeddings(chunks)
    print("Embeddings generated")

    with engine.connect() as conn:
        for chunk, embedding in zip(chunks, embeddings):
            embedding_str = format_embedding(embedding)

            conn.execute(text("""
                INSERT INTO document_embeddings (id, content, embedding)
                VALUES (:id, :content, :embedding)
            """), {
                "id": str(uuid.uuid4()),
                "content": chunk,
                "embedding": embedding_str
            })

        conn.commit()

    print("DB insert done")

    return {"status": f"{len(chunks)} chunks stored"}

def search_similar(query_text: str):
    embedding_list = get_embeddings([query_text])   # returns list
    query_embedding = embedding_list[0]             # take FIRST

    embedding_str = "[" + ",".join(map(str, query_embedding)) + "]"

    query = text("""
        SELECT content
        FROM document_embeddings
        ORDER BY embedding <-> :embedding
        LIMIT 3
    """)

    with engine.connect() as conn:
        result = conn.execute(query, {
            "embedding": embedding_str
        })
        return [row[0] for row in result.fetchall()]


def chunk_text(text: str, chunk_size: int = 300):
    words = text.split()
    chunks = []

    for i in range(0, len(words), chunk_size):
        chunk = " ".join(words[i:i + chunk_size])
        chunks.append(chunk)

    return chunks

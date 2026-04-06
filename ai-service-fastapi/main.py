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

    print("\n===== RAG DEBUG START =====")
    print("USER QUERY:", user_query)

    # Step 2: Fetch relevant context from DB
    context_docs = search_similar(user_query)

    print("\nRETRIEVED CHUNKS:")
    for i, c in enumerate(context_docs):
        print(f"{i+1}.", c)

    context_text = "\n".join(context_docs)

    print("\nFINAL CONTEXT SENT TO LLM:")
    print(context_text)
    print("===== RAG DEBUG END =====\n")

    # Step 3: Updated system prompt with RAG
    messages = [
        {
            "role": "system",
            "content": f"""
You are an assistant.

Answer the question using ONLY the context below.

If the answer is present, extract it clearly.

Context:
{context_text}
"""
        }
    ]

    # Step 4: Add full conversation
    for msg in request.messages:
        messages.append({
            "role": msg.role.lower(),
            "content": msg.content
        })

    # Step 5: Call LLM
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
        SELECT content, embedding <-> :embedding AS distance
        FROM document_embeddings
        ORDER BY distance ASC
        LIMIT 10
    """)

    with engine.connect() as conn:
        result = conn.execute(query, {
            "embedding": embedding_str
        })

        rows = result.fetchall()

        # debug distances
        for r in rows:
            print("DIST:", r[1], "| TEXT:", r[0])

        return [r[0] for r in rows]


def chunk_text(text: str):
    chunks = [chunk.strip() for chunk in text.split("\n\n") if chunk.strip()]
    return chunks

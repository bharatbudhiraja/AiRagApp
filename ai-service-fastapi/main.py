import os
from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
from openai import OpenAI
from dotenv import load_dotenv

# Load env variables
load_dotenv()

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

    # Add system prompt
    messages = [
        {"role": "system", "content": "You are an expert Android developer."}
    ]

    # Add full conversation from Spring Boot
    for msg in request.messages:
        messages.append({
            "role": msg.role.lower(),   # normalize just in case
            "content": msg.content
        })

    response = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=messages
    )

    return {
        "reply": response.choices[0].message.content
    }
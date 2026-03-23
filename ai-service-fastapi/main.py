import os
from fastapi import FastAPI
from pydantic import BaseModel
from openai import OpenAI
from dotenv import load_dotenv

# Load env variables
load_dotenv()

# Create app
app = FastAPI()

# Initialize client (GitHub models for now)
client = OpenAI(
    api_key=os.getenv("GITHUB_TOKEN"),
    base_url="https://models.inference.ai.azure.com"
)

# Request model (like Kotlin data class)
class ChatRequest(BaseModel):
    message: str
    history: list = []


@app.post("/chat")
def chat(request: ChatRequest):
    messages = [
        {"role": "system", "content": "You are an expert Android developer."}
    ]

    # Add history
    for msg in request.history:
        messages.append(msg)

    # Add current message
    messages.append({"role": "user", "content": request.message})

    response = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=messages
    )

    return {
        "response": response.choices[0].message.content
    }
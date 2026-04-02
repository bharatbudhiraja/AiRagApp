# 🧠 AI RAG Chatbot (Android + Spring Boot + FastAPI)

A full-stack AI-powered chatbot implementing **Retrieval-Augmented Generation (RAG)** using a modern multi-layer architecture.

---

## 🏗️ Architecture

```
Android (Jetpack Compose)
        ↓
Spring Boot (Kotlin Backend API)
        ↓
FastAPI (Python AI Service)
        ↓
PostgreSQL (Neon) + pgvector
```

---

## 🚀 Features

### 💬 Chat System

* Real-time chat interface (Jetpack Compose)
* Conversation-based messaging
* Multi-screen navigation (Conversations + Chat)

### 🧠 AI Integration

* LLM-powered responses (GPT-4o-mini)
* Context-aware replies using RAG
* Hybrid mode (uses context when relevant, otherwise general knowledge)

### 🔍 Retrieval-Augmented Generation (RAG)

* Vector embeddings stored in PostgreSQL (pgvector)
* Semantic search for relevant context
* Dynamic context injection into prompts

### 📦 Data Processing

* Document ingestion via FastAPI
* Chunking strategy (paragraph-based)
* Batch embedding generation (optimized)

### ⚙️ Backend Design

* Spring Boot handles business logic & chat orchestration
* FastAPI handles AI logic (embeddings + retrieval + LLM)
* Shared PostgreSQL database

---

## 🧱 Tech Stack

### Mobile

* Kotlin
* Jetpack Compose
* Navigation Compose
* Hilt (DI)

### Backend

* Spring Boot (Kotlin)
* REST APIs

### AI Service

* FastAPI (Python)
* OpenAI API (LLM + Embeddings)

### Database

* PostgreSQL (Neon)
* pgvector (vector search)

---

## 🔁 Chat Flow

```
User → Android App
      ↓
Spring Boot (/chat/send)
      ↓
FastAPI (/chat)
      ↓
[1] Generate query embedding
[2] Retrieve similar documents (pgvector)
[3] Build context
[4] Call LLM
      ↓
Response → Android UI
```

---

## 🧪 RAG Flow

```
Text → Chunking → Embeddings → Store in DB
Query → Embedding → Similarity Search → Context → LLM
```

---

## ⚙️ Setup

### 1. Clone Repository

```
git clone <your-repo>
```

---

### 2. FastAPI Setup

* Create `.env` file:

```
OPENAI_API_KEY=your_key
DATABASE_URL=your_postgres_url
```

* Install dependencies:

```
pip install -r requirements.txt
```

* Run FastAPI:

```
python -m uvicorn main:app --reload
```

---

### 3. Spring Boot Setup

* Configure DB connection
* Run application

---

### 4. Android App

* Open project in Android Studio
* Run on emulator/device

---

## 🧠 Key Learnings

* Built end-to-end RAG system from scratch
* Implemented vector search using pgvector
* Designed scalable multi-service architecture
* Integrated AI into mobile application
* Handled real-world issues (chunking, retrieval accuracy, API latency)

---

## 🚀 Future Improvements

* Similarity threshold filtering
* Source attribution in responses
* Pinecone / Weaviate integration
* Streaming responses
* User-specific knowledge base
* PDF / document upload support

---

## 📌 Status

✅ Core RAG working
✅ Chat integration complete
🔄 Optimization in progress

---

## 👨‍💻 Author

Bharat Budhiraja


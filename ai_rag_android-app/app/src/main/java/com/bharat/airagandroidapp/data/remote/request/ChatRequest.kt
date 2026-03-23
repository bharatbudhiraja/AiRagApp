package com.bharat.airagandroidapp.data.remote.request

data class ChatRequest(
    val message: String,
    val history: List<Map<String, String>>
)

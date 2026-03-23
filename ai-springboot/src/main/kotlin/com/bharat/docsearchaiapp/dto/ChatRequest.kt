package com.bharat.docsearchaiapp.dto

data class ChatRequest(
    val message: String,
    val history: List<Map<String, String>> = emptyList()
)

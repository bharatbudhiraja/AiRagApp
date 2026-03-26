package com.bharat.docsearchaiapp.dto

import java.util.UUID

data class ChatRequest(
//    val message: String,
//    val history: List<Map<String, String>> = emptyList()
    val conversationId: UUID,
    val message: String
)


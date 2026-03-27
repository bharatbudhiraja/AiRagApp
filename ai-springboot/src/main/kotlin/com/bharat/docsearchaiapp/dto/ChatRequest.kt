package com.bharat.docsearchaiapp.dto

import java.util.UUID

data class ChatRequest(
    val conversationId: UUID?,
    val message: String
)


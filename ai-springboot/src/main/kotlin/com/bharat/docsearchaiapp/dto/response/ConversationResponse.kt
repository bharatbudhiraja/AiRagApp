package com.bharat.docsearchaiapp.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class ConversationResponse(
    val id: UUID,
    val title: String?,
    val createdAt: LocalDateTime
)
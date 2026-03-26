package com.bharat.docsearchaiapp.dto.response

import com.bharat.docsearchaiapp.entity.Role
import java.time.LocalDateTime
import java.util.UUID

data class MessageResponse(
    val id: UUID,
    val role: Role?,
    val content: String?,
    val createdAt: LocalDateTime
)

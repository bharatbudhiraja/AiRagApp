package com.bharat.docsearchaiapp.dto.response

import java.util.UUID
import kotlin.uuid.Uuid

data class ChatResponse(
    val reply: String,
    val uuid: UUID
)
package com.bharat.airagandroidapp.data.remote.response

data class MessageResponse(
    val id: String,
    val role: String?,
    val content: String?,
    val createdAt: String
)

package com.bharat.airagandroidapp.ui.theme.chat

import com.bharat.airagandroidapp.domain.ChatMessage


data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
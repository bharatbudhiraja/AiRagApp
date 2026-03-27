package com.bharat.airagandroidapp.ui.theme.conversations

import com.bharat.airagandroidapp.domain.Conversation


data class ConversationUiState(
    val conversations: List<Conversation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
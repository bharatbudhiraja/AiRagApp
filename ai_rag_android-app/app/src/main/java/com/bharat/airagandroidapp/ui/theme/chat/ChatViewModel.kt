package com.bharat.airagandroidapp.ui.theme.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.airagandroidapp.data.repository.ChatRepository
import com.bharat.airagandroidapp.domain.ChatMessage
import com.bharat.airagandroidapp.domain.enum.Role
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state

    private val conversationId = "11111111-1111-1111-1111-111111111111"

    fun sendMessage(message: String) {

        // 1. Add user message immediately
        val updatedMessages = _state.value.messages + ChatMessage(
            message,
            true
        )

        _state.value = _state.value.copy(
            messages = updatedMessages,
            isLoading = true,
            error = null
        )

        // 2. Call backend
        viewModelScope.launch {
            try {
                val reply = repository.sendMessage(conversationId, message)

                val newMessages = _state.value.messages + ChatMessage(
                    text = reply ?: "No response",
                    isUser = false
                )

                _state.value = _state.value.copy(
                    messages = newMessages,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadMessages() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getMessages(conversationId)

                val messages = if (response.success) {
                    response.data?.map {
                        ChatMessage(
                            text = it.content ?: "",
                            isUser = it.role == Role.USER.name
                        )
                    } ?: emptyList()
                } else {
                    throw Exception(response.error?.message)
                }

                _state.value = _state.value.copy(
                    messages = messages,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
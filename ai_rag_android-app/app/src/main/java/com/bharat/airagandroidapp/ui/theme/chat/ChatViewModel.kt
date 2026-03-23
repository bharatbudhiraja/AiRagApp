package com.bharat.airagandroidapp.ui.theme.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.airagandroidapp.data.repository.ChatRepository
import com.bharat.airagandroidapp.domain.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        val currentMessages = _uiState.value.messages

        val newMessages = currentMessages.takeLast(5) + ChatMessage(text, true)

        _uiState.value = _uiState.value.copy(
            messages = newMessages,
            isLoading = true
        )

        viewModelScope.launch {
            try {
                val history = currentMessages.map {
                    mapOf(
                        "role" to if (it.isUser) "user" else "assistant",
                        "content" to it.text
                    )
                }

                val response = repository.sendMessage(text, history)

                _uiState.value = _uiState.value.copy(
                    messages = newMessages + ChatMessage(response, false),
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
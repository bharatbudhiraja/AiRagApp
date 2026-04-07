package com.bharat.airagandroidapp.ui.theme.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bharat.airagandroidapp.data.repository.ChatRepository
import com.bharat.airagandroidapp.domain.ChatMessage
import com.bharat.airagandroidapp.domain.Conversation
import com.bharat.airagandroidapp.domain.enum.Role
import com.bharat.airagandroidapp.ui.theme.conversations.ConversationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _stateMessages = MutableStateFlow(ChatUiState())
    val stateMessages: StateFlow<ChatUiState> = _stateMessages

    private val _stateConversations = MutableStateFlow(ConversationUiState())
    val stateConversations: StateFlow<ConversationUiState> = _stateConversations

    private var conversationId: String? = null

    fun setConversation(id: String?) {
        conversationId = id
    }
    fun sendMessage(message: String) {

        // 1. Add user message immediately
        val updatedMessages = _stateMessages.value.messages + ChatMessage(
            message,
            true
        )

        _stateMessages.value = _stateMessages.value.copy(
            messages = updatedMessages,
            isLoading = true,
            error = null
        )

        // 2. Call backend
        viewModelScope.launch {
            try {
                val reply = repository.sendMessage(conversationId, message)

                Log.d("CHAT_DEBUG", "Body: $reply")

                val newMessages = _stateMessages.value.messages + ChatMessage(
                    text = reply?.reply ?: "No response",
                    isUser = false
                )
                conversationId = reply?.uuid

                _stateMessages.value = _stateMessages.value.copy(
                    messages = newMessages,
                    isLoading = false
                )


            } catch (e: Exception) {
                Log.e("CHAT_DEBUG", "Error: ${e.message}")
                _stateMessages.value = _stateMessages.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadMessages() {
        if(conversationId == null)
            return
        _stateMessages.value = _stateMessages.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response = repository.getMessages(conversationId?:"")

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

                _stateMessages.value = _stateMessages.value.copy(
                    messages = messages,
                    isLoading = false
                )

            } catch (e: Exception) {
                _stateMessages.value = _stateMessages.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadConversations() {
        _stateConversations.value = _stateConversations.value.copy(isLoading = true)
        viewModelScope.launch {
            try{
                val response = repository.getConversations()
                val conversations = if (response.success) {
                    response.data?.map {
                        Conversation(
                            id = it.id,
                            text = it.title ?: "",
                            createdAt = it.createdAt
                        )
                    } ?: emptyList()
                } else {
                    throw Exception(response.error?.message)
                }

                _stateConversations.value = _stateConversations.value.copy(
                    conversations = conversations,
                    isLoading = false
                )
            }catch (e : Exception){
                _stateConversations.value = _stateConversations.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
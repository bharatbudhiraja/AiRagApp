package com.bharat.airagandroidapp.data.repository

import com.bharat.airagandroidapp.data.remote.ApiService
import com.bharat.airagandroidapp.data.remote.request.ChatRequest
import javax.inject.Inject


class ChatRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getConversations() = api.getConversations()

    suspend fun getMessages(conversationId: String) =
        api.getMessages(conversationId)

    suspend fun sendMessage(conversationId: String, message: String): String? {
        val response = api.sendMessage(ChatRequest(message, conversationId))

        return if (response.success) {
            response.data?.reply
        } else {
            throw Exception(response.error?.message)
        }
    }
}
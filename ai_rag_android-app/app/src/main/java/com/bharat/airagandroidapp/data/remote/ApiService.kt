package com.bharat.airagandroidapp.data.remote

import com.bharat.airagandroidapp.data.remote.request.ChatRequest
import com.bharat.airagandroidapp.data.remote.response.ApiResponse
import com.bharat.airagandroidapp.data.remote.response.ChatResponse
import com.bharat.airagandroidapp.data.remote.response.ConversationResponse
import com.bharat.airagandroidapp.data.remote.response.MessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/chat/send")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ApiResponse<ChatResponse>

    @GET("/chat/conversations")
    suspend fun getConversations(): ApiResponse<List<ConversationResponse>>

    @GET("/chat/messages")
    suspend fun getMessages(
        @Query("conversationId") conversationId: String
    ): ApiResponse<List<MessageResponse>>
}
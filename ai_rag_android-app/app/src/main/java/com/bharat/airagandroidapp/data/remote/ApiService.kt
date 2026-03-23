package com.bharat.airagandroidapp.data.remote

import com.bharat.airagandroidapp.data.remote.request.ChatRequest
import com.bharat.airagandroidapp.data.remote.response.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/chat")
    suspend fun chat(
        @Body request: ChatRequest
    ): ChatResponse
}
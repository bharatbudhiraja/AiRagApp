package com.bharat.airagandroidapp.data.repository

import android.util.Log
import com.bharat.airagandroidapp.data.remote.ApiService
import com.bharat.airagandroidapp.data.remote.request.ChatRequest
import javax.inject.Inject


class ChatRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun sendMessage(
        message: String,
        history: List<Map<String, String>>
    ): String {
        return try {
            val response = api.chat(ChatRequest(message, history))
            Log.d("API_DEBUG", "Response: $response")
            response.response
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error: ${e.message}", e)
            "Error: ${e.message}"
        }
    }
}
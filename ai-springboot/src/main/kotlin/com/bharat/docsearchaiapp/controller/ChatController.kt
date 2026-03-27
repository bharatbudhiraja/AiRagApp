package com.bharat.docsearchaiapp.controller

import com.bharat.docsearchaiapp.dto.ChatRequest
import com.bharat.docsearchaiapp.dto.response.ApiResponse
import com.bharat.docsearchaiapp.dto.response.ChatResponse
import com.bharat.docsearchaiapp.dto.response.ConversationResponse
import com.bharat.docsearchaiapp.dto.response.MessageResponse
import com.bharat.docsearchaiapp.service.ChatService
import org.apache.juli.logging.Log
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.UUID

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping("/send")
    fun sendMessage(@RequestBody request: ChatRequest): ApiResponse<ChatResponse> {
        System.out.println("Request..."+request.message)
        val reply = chatService.sendMessage(request)
        return ApiResponse(
            success = true,
            data = ChatResponse(reply.reply, reply.uuid),
            error = null
        )
    }

    @GetMapping("/conversations")
    fun getConversations(): ApiResponse<List<ConversationResponse>> {
        val data = chatService.getAllConversations()
        return ApiResponse(true, data, null)
    }

    @GetMapping("/messages")
    fun getMessages(@RequestParam conversationId: UUID): ApiResponse<List<MessageResponse>> {
        val data = chatService.getMessages(conversationId)
        return ApiResponse(true, data, null)
    }
}
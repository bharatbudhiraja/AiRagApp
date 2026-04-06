package com.bharat.docsearchaiapp.service

import com.bharat.docsearchaiapp.dto.ChatRequest
import com.bharat.docsearchaiapp.dto.response.ChatResponse
import com.bharat.docsearchaiapp.dto.response.ConversationResponse
import com.bharat.docsearchaiapp.dto.response.MessageResponse
import com.bharat.docsearchaiapp.entity.Conversation
import com.bharat.docsearchaiapp.entity.Message
import com.bharat.docsearchaiapp.entity.Role
import com.bharat.docsearchaiapp.repository.ConversationRepository
import com.bharat.docsearchaiapp.repository.MessageRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.UUID

@Service
class ChatService(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository
) {
    private val restTemplate = RestTemplate()
    private val fastApiUrl = "http://127.0.0.1:8000/chat"

    fun sendMessage(request: ChatRequest): ChatResponse {

        val conversation = if (request.conversationId == null) {

            val newConv = Conversation(
                id = UUID.randomUUID(),   // ALWAYS generate here
                title = request.message.take(20)
            )
            conversationRepository.save(newConv)

        } else {

            conversationRepository.findById(request.conversationId)
                .orElseThrow {
                    RuntimeException("Conversation not found")
                }
        }
        // 2. Save user message
        val userMessage = Message(
            conversationId = conversation.id,
            role = Role.USER,
            content = request.message
        )
        messageRepository.save(userMessage)

        // 3. Fetch conversation history (limit last 20)
        val messages = messageRepository
            .findByConversationIdOrderByCreatedAtAsc(conversation.id)
            .takeLast(20)

        // 4. Convert to FastAPI format
        val aiRequest = mapOf(
            "messages" to messages.map {
                mapOf(
                    "role" to it.role,
                    "content" to it.content
                )
            }
        )

        // 5. Call FastAPI
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity(aiRequest, headers)

        val response = restTemplate.postForEntity(
            fastApiUrl,
            entity,
            Map::class.java
        )

        val aiReplyText = response.body?.get("reply") as String

        // 6. Save AI response
        val aiMessage = Message(
            conversationId = conversation.id,
            role = Role.ASSISTANT,
            content = aiReplyText
        )
        messageRepository.save(aiMessage)

        // 7. Return response
        return ChatResponse(aiReplyText, conversation.id)
    }

    fun getAllConversations(): List<ConversationResponse> {
        return conversationRepository.findAll()
            .sortedByDescending { it.createdAt }
            .map {
                ConversationResponse(
                    id = it.id,
                    title = it.title,
                    createdAt = it.createdAt
                )
            }
    }

    fun getMessages(conversationId: UUID): List<MessageResponse> {
        return messageRepository
            .findByConversationIdOrderByCreatedAtAsc(conversationId)
            .map {
                MessageResponse(
                    id = it.id,
                    role = it.role,
                    content = it.content,
                    createdAt = it.createdAt
                )
            }
    }
}
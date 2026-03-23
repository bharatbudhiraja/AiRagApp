package com.bharat.docsearchaiapp.controller

import com.bharat.docsearchaiapp.dto.ChatRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@RestController
@RequestMapping("/api")
class ChatController {
    private val restTemplate = RestTemplate()
    private val fastApiUrl = "http://127.0.0.1:8000/chat"
    @PostMapping("/chat")
    fun chat(@RequestBody request: ChatRequest): ResponseEntity<String> {
        println("Incoming request: $request")
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity(request, headers)

        val response = restTemplate.postForEntity<String>(
            fastApiUrl,
            entity
        )

        return ResponseEntity.ok(response.body)
    }
}
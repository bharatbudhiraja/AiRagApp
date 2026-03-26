package com.bharat.docsearchaiapp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Message(
    @Id
    var id: UUID = UUID.randomUUID(),

    var conversationId: UUID? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER,

    @Column(columnDefinition = "TEXT")
    var content: String? = null,

    var createdAt: LocalDateTime = LocalDateTime.now()
)

enum class Role {
    USER,
    ASSISTANT
}
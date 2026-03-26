package com.bharat.docsearchaiapp.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Conversation(@Id
                        val id: UUID = UUID.randomUUID(),

                        val title: String? = null,

                        val createdAt: LocalDateTime = LocalDateTime.now())

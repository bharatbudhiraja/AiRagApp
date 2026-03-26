package com.bharat.docsearchaiapp.repository

import com.bharat.docsearchaiapp.entity.Conversation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ConversationRepository : JpaRepository<Conversation, UUID> {
}
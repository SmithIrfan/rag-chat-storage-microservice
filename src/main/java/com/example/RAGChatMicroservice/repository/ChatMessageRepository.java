package com.example.RAGChatMicroservice.repository;

import com.example.RAGChatMicroservice.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ChatMessage} entities.
 *
 * <p>
 * Provides CRUD operations and custom queries for chat messages.
 * </p>
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Retrieves messages belonging to a specific chat session with pagination.
     *
     * @param sessionId the ID of the chat session
     * @param pageable  pagination information
     * @return a paginated list of chat messages
     */
    Page<ChatMessage> findBySessionId(Long sessionId, Pageable pageable);
}

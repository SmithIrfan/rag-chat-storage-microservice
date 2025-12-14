package com.example.RAGChatMicroservice.repository;

import com.example.RAGChatMicroservice.entity.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link ChatSession} entities.
 *
 * <p>
 * Provides CRUD operations and custom queries for chat sessions.
 * </p>
 */
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    /**
     * Finds a chat session by ID if it is not marked as deleted.
     *
     * @param sessionId the ID of the chat session
     * @return an {@link Optional} containing the session if found and not deleted
     */
    Optional<ChatSession> findByIdAndIsDeletedFalse(Long sessionId);

    /**
     * Retrieves all chat sessions that are not marked as deleted with pagination.
     *
     * @param of pagination information
     * @return a paginated list of active chat sessions
     */
    Page<ChatSession> findByIsDeletedFalse(PageRequest of);
}

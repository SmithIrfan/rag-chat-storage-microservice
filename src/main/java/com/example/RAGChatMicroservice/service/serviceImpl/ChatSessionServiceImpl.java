package com.example.RAGChatMicroservice.service.serviceImpl;

import com.example.RAGChatMicroservice.dto.request.CreateSessionRequest;
import com.example.RAGChatMicroservice.dto.request.RenameSessionRequest;
import com.example.RAGChatMicroservice.dto.response.SessionResponse;
import com.example.RAGChatMicroservice.entity.ChatSession;
import com.example.RAGChatMicroservice.exception.ResourceNotFoundException;
import com.example.RAGChatMicroservice.repository.ChatSessionRepository;
import com.example.RAGChatMicroservice.service.ChatSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Java 21 compliant implementation of {@link ChatSessionService}.
 *
 * <p>
 * Responsible for managing the complete lifecycle of chat sessions:
 * <ul>
 *     <li>Create new chat sessions</li>
 *     <li>Retrieve single or multiple sessions</li>
 *     <li>Rename sessions</li>
 *     <li>Mark sessions as favorite</li>
 *     <li>Soft delete sessions</li>
 * </ul>
 *
 */
@Service
@Slf4j
public class ChatSessionServiceImpl implements ChatSessionService {

    /**
     * Repository for chat session persistence.
     */
    private final ChatSessionRepository sessionRepo;

    /**
     * Constructor injection ensures immutability and testability.
     */
    public ChatSessionServiceImpl(ChatSessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    /**
     * Creates a new chat session.
     *
     * <p>
     * Default values initialized:
     * <ul>
     *     <li>isFavorite = false</li>
     *     <li>isDeleted = false</li>
     *     <li>createdAt & updatedAt timestamps</li>
     * </ul>
     *
     * @param request request containing session name and optional userId
     * @return created session response
     */
    @Override
    public SessionResponse createSession(CreateSessionRequest request) {

        // Log business intent
        log.info("Creating chat session | userId={}", request.getUserId());

        // Build and persist new session entity
        ChatSession session = buildNewSession(request);
        ChatSession savedSession = sessionRepo.save(session);

        // Convert entity to response DTO
        return toResponse(savedSession);
    }

    /**
     * Retrieves a single active (non-deleted) chat session by ID.
     *
     * @param id chat session identifier
     * @return session details
     * @throws ResourceNotFoundException if session does not exist or is deleted
     */
    @Override
    public SessionResponse getSession(Long id) {

        log.info("Fetching chat session | id={}", id);

        // Validate session existence
        ChatSession session = findActiveSession(id);

        return toResponse(session);
    }

    /**
     * Retrieves all active chat sessions using pagination.
     *
     * <p>
     * Only non-deleted sessions are returned.
     *
     * @param page page number (0-based)
     * @param size page size
     * @return list of session responses
     */
    @Override
    public List<SessionResponse> getAllSessions(int page, int size) {

        log.info("Fetching chat sessions | page={} | size={}", page, size);

        return sessionRepo.findByIsDeletedFalse(PageRequest.of(page, size)).getContent().stream().map(this::toResponse).toList();
    }

    /**
     * Renames an existing chat session.
     *
     * <p>
     * Updates the session name and refreshes the updated timestamp.
     *
     * @param id      chat session identifier
     * @param request rename request containing new name
     * @return updated session response
     */
    @Override
    public SessionResponse renameSession(Long id, RenameSessionRequest request) {

        log.info("Renaming chat session | id={} | newName={}", id, request.getName());

        ChatSession session = findActiveSession(id);
        session.setSessionName(request.getName());

        // Update modification timestamp
        session.setUpdatedAt(LocalDateTime.now());

        return toResponse(sessionRepo.save(session));
    }

    /**
     * Marks or unmarks a chat session as favorite.
     *
     * @param id         chat session identifier
     * @param isFavorite favorite flag
     * @return updated session response
     */
    @Override
    public SessionResponse markFavorite(Long id, Boolean isFavorite) {

        log.info("Updating favorite flag | sessionId={} | isFavorite={}", id, isFavorite);

        ChatSession session = findActiveSession(id);
        session.setIsFavorite(isFavorite);

        session.setUpdatedAt(LocalDateTime.now());

        return toResponse(sessionRepo.save(session));
    }

    /**
     * Soft deletes a chat session.
     *
     * <p>
     * Session and its associated messages are marked as deleted
     * instead of being physically removed from the database.
     *
     * @param id chat session identifier
     * @return deletion confirmation message
     */
    @Override
    public String deleteSession(Long id) {

        log.info("Soft deleting chat session | id={}", id);

        ChatSession session = findActiveSession(id);
        session.setIsDeleted(true);
        session.setUpdatedAt(LocalDateTime.now());

        // Soft delete all messages under this session
        session.getMessages().forEach(message -> {
            message.setIsDeleted(true);
            message.setUpdatedAt(LocalDateTime.now());
        });

        sessionRepo.save(session);

        return "Session deleted successfully";
    }

    // ----------------------------------------------------------------
    // Helper Methods
    // ----------------------------------------------------------------

    /**
     * Builds a new {@link ChatSession} entity from request data.
     */
    private ChatSession buildNewSession(CreateSessionRequest request) {
        ChatSession session = new ChatSession();
        session.setSessionName(request.getSessionName());
        session.setIsFavorite(false);
        session.setIsDeleted(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        session.setUserId(request.getUserId());
        return session;
    }

    /**
     * Fetches an active (non-deleted) chat session.
     *
     * @throws ResourceNotFoundException if session is not found
     */
    private ChatSession findActiveSession(Long id) {
        return sessionRepo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Chat session not found with id: " + id));
    }


    /**
     * Maps {@link ChatSession} entity to {@link SessionResponse} DTO.
     */
    private SessionResponse toResponse(ChatSession session) {
        SessionResponse dto = new SessionResponse();
        dto.setSessionId(session.getId());
        dto.setSessionName(session.getSessionName());
        dto.setIsFavorite(session.getIsFavorite());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());
        dto.setUserId(session.getUserId());
        return dto;
    }
}

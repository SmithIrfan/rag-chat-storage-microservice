package com.example.RAGChatMicroservice.service.serviceImpl;

import com.example.RAGChatMicroservice.dto.request.AddMessageRequest;
import com.example.RAGChatMicroservice.dto.response.MessageResponse;
import com.example.RAGChatMicroservice.dto.response.SessionMessageResponse;
import com.example.RAGChatMicroservice.entity.ChatMessage;
import com.example.RAGChatMicroservice.entity.ChatSession;
import com.example.RAGChatMicroservice.exception.ResourceNotFoundException;
import com.example.RAGChatMicroservice.repository.ChatMessageRepository;
import com.example.RAGChatMicroservice.repository.ChatSessionRepository;
import com.example.RAGChatMicroservice.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing chat messages.
 *
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Add messages to an existing chat session</li>
 *     <li>Retrieve paginated messages for a session</li>
 * </ul>
 */
@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    /**
     * Repository for chat message persistence.
     */
    private final ChatMessageRepository messageRepo;

    /**
     * Repository for chat session validation and lookup.
     */
    private final ChatSessionRepository sessionRepo;

    /**
     * Constructor-based injection ensures immutability
     * and makes the service easier to test.
     */
    public ChatMessageServiceImpl(ChatMessageRepository messageRepo, ChatSessionRepository sessionRepo) {
        this.messageRepo = messageRepo;
        this.sessionRepo = sessionRepo;
    }

    /**
     * Adds a new message to an existing chat session.
     *
     * <p>
     * Flow:
     * <ol>
     *     <li>Validate that the session exists and is active</li>
     *     <li>Convert request DTO into ChatMessage entity</li>
     *     <li>Persist message to database</li>
     *     <li>Return response DTO</li>
     * </ol>
     *
     * @param sessionId unique chat session identifier
     * @param request   message payload (sender, content, context)
     * @return response containing saved message details
     */
    @Override
    public SessionMessageResponse addMessage(Long sessionId, AddMessageRequest request) {

        // Log business event (not an error)
        log.info("Adding message | sessionId={} | sender={}", sessionId, request.getSender());

        // Ensure session exists and is not soft-deleted
        ChatSession session = findActiveSession(sessionId);

        // Convert incoming request into entity
        ChatMessage message = buildChatMessage(session, request);

        // Persist message
        ChatMessage savedMessage = messageRepo.save(message);

        // Build response with newly added message
        return buildSingleMessageResponse(sessionId, savedMessage);
    }

    /**
     * Fetches paginated messages for a given chat session.
     *
     * <p>
     * Pagination is used to avoid loading large conversations into memory.
     *
     * @param sessionId chat session identifier
     * @param page      page number (0-based)
     * @param size      number of records per page
     * @return session message response with pagination metadata
     */
    @Override
    public SessionMessageResponse getMessages(Long sessionId, int page, int size) {

        // Log request context for traceability
        log.info("Fetching messages | sessionId={} | page={} | size={}", sessionId, page, size);

        // Validate session before fetching messages
        ChatSession session = findActiveSession(sessionId);

        // Create pageable request
        PageRequest pageable = PageRequest.of(page, size);

        // Fetch paginated messages
        Page<ChatMessage> messagePage = messageRepo.findBySessionId(session.getId(), pageable);

        // Convert entities to response DTO
        return buildPagedMessageResponse(session, messagePage);
    }

    // ----------------------------------------------------------------
    // Helper Methods
    // ----------------------------------------------------------------

    /**
     * Fetches an active (non-deleted) chat session.
     *
     * <p>
     * Centralizes session validation logic to avoid duplication.
     *
     * @param sessionId chat session identifier
     * @return active ChatSession
     * @throws ResourceNotFoundException if session does not exist or is deleted
     */
    private ChatSession findActiveSession(Long sessionId) {
        return sessionRepo.findByIdAndIsDeletedFalse(sessionId).orElseThrow(() -> new ResourceNotFoundException("Chat session not found with id: " + sessionId));
    }

    /**
     * Converts request DTO into {@link ChatMessage} entity.
     *
     * @param session active chat session
     * @param request incoming message request
     * @return populated ChatMessage entity
     */
    private ChatMessage buildChatMessage(ChatSession session, AddMessageRequest request) {
        ChatMessage message = new ChatMessage();
        message.setSender(request.getSender());
        message.setContent(request.getContent());
        message.setContext(request.getContext());

        // Timestamp assigned at service layer for consistency
        message.setCreatedAt(LocalDateTime.now());
        message.setSession(session);

        return message;
    }

    /**
     * Builds response for single message creation.
     *
     * @param sessionId chat session identifier
     * @param message   persisted chat message
     * @return response with single message
     */
    private SessionMessageResponse buildSingleMessageResponse(Long sessionId, ChatMessage message) {
        SessionMessageResponse response = new SessionMessageResponse();
        response.setSessionId(sessionId);
        response.setMessages(List.of(toResponse(message)));
        return response;
    }

    /**
     * Builds paginated response for message retrieval.
     *
     * @param session     chat session
     * @param messagePage paginated message result
     * @return response containing messages and pagination metadata
     */
    private SessionMessageResponse buildPagedMessageResponse(ChatSession session, Page<ChatMessage> messagePage) {

        // Convert entity list into response DTO list
        List<MessageResponse> messages = messagePage.getContent().stream().map(this::toResponse).toList();

        SessionMessageResponse response = new SessionMessageResponse();
        response.setSessionId(session.getId());
        response.setSessionName(session.getSessionName());
        response.setIsFavorite(session.getIsFavorite());
        response.setMessages(messages);
        response.setTotalMessages((int) messagePage.getTotalElements());
        response.setTotalPages(messagePage.getTotalPages());

        return response;
    }

    /**
     * Maps {@link ChatMessage} entity to {@link MessageResponse} DTO.
     *
     * @param msg chat message entity
     * @return response DTO
     */
    private MessageResponse toResponse(ChatMessage msg) {
        MessageResponse dto = new MessageResponse();
        dto.setMessageId(msg.getId());
        dto.setSender(msg.getSender());
        dto.setContent(msg.getContent());
        dto.setContext(msg.getContext());
        dto.setCreatedAt(msg.getCreatedAt());
        return dto;
    }
}

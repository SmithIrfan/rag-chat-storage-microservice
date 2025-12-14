package com.example.RAGChatMicroservice.service;

import com.example.RAGChatMicroservice.dto.request.AddMessageRequest;
import com.example.RAGChatMicroservice.dto.response.SessionMessageResponse;

/**
 * Service interface for managing chat messages within a session.
 */
public interface ChatMessageService {

    /**
     * Adds a new message to a chat session.
     *
     * @param sessionId the ID of the chat session
     * @param request   the request payload containing message details
     * @return a {@link SessionMessageResponse} with updated session messages
     */
    SessionMessageResponse addMessage(Long sessionId, AddMessageRequest request);

    /**
     * Retrieves messages of a chat session with pagination.
     *
     * @param sessionId the ID of the chat session
     * @param page      the page number (starting from 0)
     * @param size      the number of messages per page
     * @return a {@link SessionMessageResponse} containing paginated messages
     */
    SessionMessageResponse getMessages(Long sessionId, int page, int size);
}

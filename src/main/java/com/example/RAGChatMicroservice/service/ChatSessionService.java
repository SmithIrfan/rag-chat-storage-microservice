package com.example.RAGChatMicroservice.service;

import com.example.RAGChatMicroservice.dto.request.CreateSessionRequest;
import com.example.RAGChatMicroservice.dto.request.RenameSessionRequest;
import com.example.RAGChatMicroservice.dto.response.SessionResponse;

import java.util.List;


/**
 * Service interface for managing chat sessions.
 * <p>
 * Provides operations to create, retrieve, update, favorite,
 * and softly delete chat sessions.
 * </p>
 */
public interface ChatSessionService {

    /**
     * Creates a new chat session.
     *
     * @param request request object containing session creation details
     *                such as session name and user identifier
     * @return {@link SessionResponse} containing the created session details
     */
    SessionResponse createSession(CreateSessionRequest request);

    /**
     * Retrieves a chat session by its unique identifier.
     *
     * @param id unique identifier of the chat session
     * @return {@link SessionResponse} containing session details
     */
    SessionResponse getSession(Long id);

    /**
     * Retrieves all active (non-deleted) chat sessions with pagination support.
     *
     * @param page zero-based page index
     * @param size number of records per page
     * @return list of {@link SessionResponse} objects
     */
    List<SessionResponse> getAllSessions(int page, int size);

    /**
     * Renames an existing chat session.
     *
     * @param id   unique identifier of the chat session
     * @param request new request
     * @return updated {@link SessionResponse}
     */
    SessionResponse renameSession(Long id,  RenameSessionRequest request);

    /**
     * Marks or unmarks a chat session as a favorite.
     *
     * @param id         unique identifier of the chat session
     * @param isFavorite {@code true} to mark as favorite,
     *                   {@code false} to remove from favorites
     * @return updated {@link SessionResponse}
     */
    SessionResponse markFavorite(Long id, Boolean isFavorite);

    /**
     * Soft deletes a chat session.
     * <p>
     * The session is not physically removed from the database.
     * Instead, it is marked as deleted and excluded from future queries.
     * </p>
     *
     * @param id unique identifier of the chat session
     */
    String deleteSession(Long id);
}


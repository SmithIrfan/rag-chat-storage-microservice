package com.example.RAGChatMicroservice.controller;

import com.example.RAGChatMicroservice.dto.request.CreateSessionRequest;
import com.example.RAGChatMicroservice.dto.request.RenameSessionRequest;
import com.example.RAGChatMicroservice.dto.response.ApiResponse;
import com.example.RAGChatMicroservice.service.ChatSessionService;
import com.example.RAGChatMicroservice.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.RAGChatMicroservice.constants.ControllerConstants.*;

/**
 * REST controller for managing chat sessions.
 *
 * <p>
 * Provides endpoints to create, retrieve, update, mark favorite, and delete chat sessions.
 * Responses are consistently formatted using {@link ResponseUtils}.
 * </p>
 */
@RestController
@RequestMapping(SESSIONS_BASE_URL)
public class ChatSessionController {

    @Autowired
    private ChatSessionService chatSessionService;

    /**
     * Creates a new chat session.
     *
     * @param request the request payload containing session details
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 201 CREATED} and the created session data
     */
    @PostMapping(value = CREATE_SESSION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createSession(@RequestBody CreateSessionRequest request) {
        return ResponseUtils.getResponseEntity(HttpStatus.CREATED, SUCCESS, chatSessionService.createSession(request));
    }

    /**
     * Retrieves a chat session by its ID.
     *
     * @param sessionId the ID of the chat session
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 200 OK} and the session details
     */
    @GetMapping(value = SESSION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getSession(@PathVariable Long sessionId) {
        return ResponseUtils.getResponseEntity(HttpStatus.OK, SUCCESS, chatSessionService.getSession(sessionId));
    }

    /**
     * Retrieves all chat sessions with pagination.
     *
     * @param page the page number (default is 0)
     * @param size the number of sessions per page (default is 10)
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 200 OK} and the paginated list of sessions
     */
    @GetMapping(value = GET_ALL_SESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllSessions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseUtils.getResponseEntity(HttpStatus.OK, SUCCESS, chatSessionService.getAllSessions(page, size));
    }

    /**
     * Renames an existing chat session.
     *
     * @param sessionId the ID of the chat session
     * @param request   the request payload containing the new session name
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 200 OK} and the updated session details
     */
    @PatchMapping(value = RENAME_SESSION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> renameSession(@PathVariable Long sessionId, @RequestBody RenameSessionRequest request) {
        return ResponseUtils.getResponseEntity(HttpStatus.OK, SUCCESS, chatSessionService.renameSession(sessionId, request));
    }

    /**
     * Marks or unmarks a chat session as favorite.
     *
     * @param sessionId  the ID of the chat session
     * @param isFavorite whether the session should be marked as favorite
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 200 OK} and the updated favorite status
     */
    @PatchMapping(value = FAVORITE_SESSION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> markFavorite(@PathVariable Long sessionId, @RequestParam Boolean isFavorite) {
        return ResponseUtils.getResponseEntity(HttpStatus.OK, SUCCESS, chatSessionService.markFavorite(sessionId, isFavorite));
    }

    /**
     * Soft deletes a chat session by its ID.
     *
     * @param sessionId the ID of the chat session
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 200 OK} and the deletion result
     */
    @DeleteMapping(value = SESSION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteSession(@PathVariable Long sessionId) {
        return ResponseUtils.getResponseEntity(HttpStatus.OK, SUCCESS, chatSessionService.deleteSession(sessionId));
    }
}

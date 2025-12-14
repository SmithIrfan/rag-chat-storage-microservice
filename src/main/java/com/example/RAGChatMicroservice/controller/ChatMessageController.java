package com.example.RAGChatMicroservice.controller;

import com.example.RAGChatMicroservice.dto.request.AddMessageRequest;
import com.example.RAGChatMicroservice.dto.response.ApiResponse;
import com.example.RAGChatMicroservice.service.ChatMessageService;
import com.example.RAGChatMicroservice.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.RAGChatMicroservice.constants.ControllerConstants.*;

/**
 * REST controller for managing chat messages under a chat session.
 *
 * <p>
 * Provides endpoints to add messages to a session and retrieve messages
 * with pagination support. Responses are consistently formatted using
 * {@link ResponseUtils}.
 * </p>
 *
 * <p>
 * This controller follows the same response structure and formatting
 * as {@link ChatSessionController}.
 * </p>
 */
@RestController
@RequestMapping(SESSIONS_BASE_URL)
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * Adds a new message to a chat session.
     *
     * @param sessionId the ID of the chat session
     * @param request   the request payload containing message details
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 201 CREATED} and the created message data
     */
    @PostMapping(value = ADD_MESSAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addMessage(@PathVariable Long sessionId, @RequestBody AddMessageRequest request) {
        return ResponseUtils.getResponseEntity(HttpStatus.CREATED, SUCCESS, chatMessageService.addMessage(sessionId, request));
    }

    /**
     * Retrieves messages of a chat session with pagination.
     *
     * @param sessionId the ID of the chat session
     * @param page      the page number (default is 0)
     * @param size      the number of messages per page (default is 20)
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     * HTTP status {@code 200 OK} and the paginated list of messages
     */
    @GetMapping(value = GET_MESSAGES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getMessages(@PathVariable Long sessionId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return ResponseUtils.getResponseEntity(HttpStatus.OK, SUCCESS, chatMessageService.getMessages(sessionId, page, size));
    }
}

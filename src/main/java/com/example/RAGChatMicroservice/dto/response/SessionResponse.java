package com.example.RAGChatMicroservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO representing a chat session.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {

    /** Unique identifier of the chat session. */
    private Long sessionId;

    /** Identifier of the user who owns the session. */
    private String userId;

    /** Name of the chat session. */
    private String sessionName;

    /** Indicates whether the session is marked as favorite. */
    private Boolean isFavorite;

    /** Timestamp when the session was created. */
    private LocalDateTime createdAt;

    /** Timestamp when the session was last updated. */
    private LocalDateTime updatedAt;
}

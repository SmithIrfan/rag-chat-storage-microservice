package com.example.RAGChatMicroservice.dto.request;

import lombok.Data;

/**
 * Request DTO for creating a new chat session.
 *
 * <p>
 * This class encapsulates the details required when a client
 * initiates a new chat session in the system.
 * </p>
 */
@Data
public class CreateSessionRequest {

    /** The identifier of the user creating the session. */
    private String userId;

    /** The human-readable name assigned to the session. */
    private String sessionName;
}

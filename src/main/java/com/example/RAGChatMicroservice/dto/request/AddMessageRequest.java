package com.example.RAGChatMicroservice.dto.request;

import lombok.Data;

/**
 * Request DTO for adding a new message to a chat session.
 *
 * <p>
 * This class encapsulates the details required when a client
 * submits a message to be stored under a specific chat session.
 * </p>
 *
 *
 */
@Data
public class AddMessageRequest {

    /** The identity of the message sender (e.g., "user", "system"). */
    private String sender;

    /** The actual text content of the message. */
    private String content;

    /** Optional RAG context providing metadata or background for the message. */
    private String context;
}

package com.example.RAGChatMicroservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO representing a chat message.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {

    /** Unique identifier of the message. */
    private Long messageId;

    /** The sender of the message (e.g., user, system). */
    private String sender;

    /** The text content of the message. */
    private String content;

    /** Optional RAG context associated with the message. */
    private String context;

    /** Timestamp when the message was created. */
    private LocalDateTime createdAt;
}

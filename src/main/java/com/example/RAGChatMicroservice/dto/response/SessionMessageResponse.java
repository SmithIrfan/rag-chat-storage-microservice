package com.example.RAGChatMicroservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO representing messages within a chat session.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionMessageResponse {

    /** Unique identifier of the chat session. */
    private Long sessionId;

    /** Name of the chat session. */
    private String sessionName;

    /** Indicates whether the session is marked as favorite. */
    private Boolean isFavorite;

    /** List of messages belonging to the session. */
    private List<MessageResponse> messages;

    /** Total number of messages in the session. */
    private Integer totalMessages;

    /** Total number of pages available for pagination. */
    private Integer totalPages;
}

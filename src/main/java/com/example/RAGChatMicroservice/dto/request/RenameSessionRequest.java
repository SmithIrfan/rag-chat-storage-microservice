package com.example.RAGChatMicroservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for renaming a chat session.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameSessionRequest {

    /** The new name for the chat session (must not be blank). */
    @NotBlank
    private String name;
}

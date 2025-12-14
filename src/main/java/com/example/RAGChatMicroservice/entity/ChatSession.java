package com.example.RAGChatMicroservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "chat_sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")   // explicit primary key column
    private Long id;

    @Column(name = "user_id", nullable = true) // optional
    private String userId;

    @Column(name = "session_name", nullable = false)
    private String sessionName;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;
}

package com.example.RAGChatMicroservice.service.serviceImpl;

import com.example.RAGChatMicroservice.dto.request.AddMessageRequest;
import com.example.RAGChatMicroservice.dto.response.SessionMessageResponse;
import com.example.RAGChatMicroservice.entity.ChatMessage;
import com.example.RAGChatMicroservice.entity.ChatSession;
import com.example.RAGChatMicroservice.exception.ResourceNotFoundException;
import com.example.RAGChatMicroservice.repository.ChatMessageRepository;
import com.example.RAGChatMicroservice.repository.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMessageServiceImplTest {

    @Mock
    private ChatMessageRepository messageRepo;

    @Mock
    private ChatSessionRepository sessionRepo;

    @InjectMocks
    private ChatMessageServiceImpl messageService;

    private ChatSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        session = new ChatSession();
        session.setId(1L);
        session.setSessionName("Test Session");
        session.setIsFavorite(false);
        session.setIsDeleted(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void addMessage_ShouldReturnResponse_WhenSessionExists() {
        AddMessageRequest request = new AddMessageRequest();

        ChatMessage savedMessage = new ChatMessage();
        savedMessage.setId(100L);
        savedMessage.setSender("Alice");
        savedMessage.setContent("Hello World");
        savedMessage.setContext("context");
        savedMessage.setCreatedAt(LocalDateTime.now());
        savedMessage.setSession(session);

        when(sessionRepo.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(session));
        when(messageRepo.save(any(ChatMessage.class))).thenReturn(savedMessage);

        SessionMessageResponse response = messageService.addMessage(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getSessionId());
        assertEquals(1, response.getMessages().size());
        assertEquals("Alice", response.getMessages().get(0).getSender());
        verify(messageRepo, times(1)).save(any(ChatMessage.class));
    }

    @Test
    void addMessage_ShouldThrowException_WhenSessionNotFound() {
        AddMessageRequest request = new AddMessageRequest();
        when(sessionRepo.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messageService.addMessage(99L, request));
    }

    @Test
    void getMessages_ShouldReturnPaginatedResponse() {
        ChatMessage msg = new ChatMessage();
        msg.setId(200L);
        msg.setSender("Charlie");
        msg.setContent("Paginated message");
        msg.setContext("ctx");
        msg.setCreatedAt(LocalDateTime.now());
        msg.setSession(session);

        when(sessionRepo.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(session));
        when(messageRepo.findBySessionId(eq(1L), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(msg)));

        SessionMessageResponse response = messageService.getMessages(1L, 0, 10);

        assertNotNull(response);
        assertEquals(1L, response.getSessionId());
        assertEquals("Test Session", response.getSessionName());
        assertEquals(1, response.getTotalMessages());
        assertEquals(1, response.getTotalPages());
        assertEquals("Charlie", response.getMessages().get(0).getSender());
    }

    @Test
    void getMessages_ShouldThrowException_WhenSessionNotFound() {
        when(sessionRepo.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messageService.getMessages(99L, 0, 10));
    }
}

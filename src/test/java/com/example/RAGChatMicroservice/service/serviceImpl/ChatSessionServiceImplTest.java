package com.example.RAGChatMicroservice.service.serviceImpl;

import com.example.RAGChatMicroservice.dto.request.CreateSessionRequest;
import com.example.RAGChatMicroservice.dto.request.RenameSessionRequest;
import com.example.RAGChatMicroservice.dto.response.SessionResponse;
import com.example.RAGChatMicroservice.entity.ChatSession;
import com.example.RAGChatMicroservice.exception.ResourceNotFoundException;
import com.example.RAGChatMicroservice.repository.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatSessionServiceImplTest {

    @Mock
    private ChatSessionRepository sessionRepo;

    @InjectMocks
    private ChatSessionServiceImpl sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSession_ShouldReturnSessionResponse() {
        CreateSessionRequest request = new CreateSessionRequest();
        ChatSession saved = new ChatSession();
        saved.setId(1L);
        saved.setSessionName("Test Session");
        saved.setUserId("user123");
        saved.setIsFavorite(false);
        saved.setIsDeleted(false);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());

        when(sessionRepo.save(any(ChatSession.class))).thenReturn(saved);

        SessionResponse response = sessionService.createSession(request);

        assertNotNull(response);
        assertEquals("Test Session", response.getSessionName());
        assertEquals("user123", response.getUserId());
        verify(sessionRepo, times(1)).save(any(ChatSession.class));
    }

    @Test
    void getSession_ShouldReturnSessionResponse_WhenSessionExists() {
        ChatSession session = new ChatSession();
        session.setId(1L);
        session.setSessionName("Existing Session");
        session.setIsDeleted(false);

        when(sessionRepo.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(session));

        SessionResponse response = sessionService.getSession(1L);

        assertEquals("Existing Session", response.getSessionName());
    }

    @Test
    void getSession_ShouldThrowException_WhenSessionNotFound() {
        when(sessionRepo.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sessionService.getSession(99L));
    }

    @Test
    void getAllSessions_ShouldReturnListOfResponses() {
        ChatSession session = new ChatSession();
        session.setId(1L);
        session.setSessionName("Session One");
        session.setIsDeleted(false);

        when(sessionRepo.findByIsDeletedFalse(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(session)));

        List<SessionResponse> responses = sessionService.getAllSessions(0, 10);

        assertEquals(1, responses.size());
        assertEquals("Session One", responses.get(0).getSessionName());
    }

    @Test
    void renameSession_ShouldUpdateName() {
        ChatSession session = new ChatSession();
        session.setId(1L);
        session.setSessionName("Old Name");
        session.setIsDeleted(false);

        when(sessionRepo.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(ChatSession.class))).thenReturn(session);

        RenameSessionRequest request = new RenameSessionRequest("New Name");
        SessionResponse response = sessionService.renameSession(1L, request);

        assertEquals("New Name", response.getSessionName());
    }

    @Test
    void markFavorite_ShouldUpdateFavoriteFlag() {
        ChatSession session = new ChatSession();
        session.setId(1L);
        session.setIsFavorite(false);
        session.setIsDeleted(false);

        when(sessionRepo.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(ChatSession.class))).thenReturn(session);

        SessionResponse response = sessionService.markFavorite(1L, true);

        assertTrue(response.getIsFavorite());
    }

    @Test
    void deleteSession_ShouldSoftDeleteSession() {
        ChatSession session = new ChatSession();
        session.setId(1L); // match the ID being requested
        session.setIsDeleted(false);
        session.setMessages(new ArrayList<>()); //  prevent NPE

        when(sessionRepo.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(ChatSession.class))).thenReturn(session);

        String result = sessionService.deleteSession(1L);

        assertEquals("Session deleted successfully", result);
        assertTrue(session.getIsDeleted());
    }


}

package com.example.RAGChatMicroservice.constants;

public class ControllerConstants {
    public static final String SESSIONS_BASE_URL = "v1/vp/sessions";
    public static final String CREATE_SESSION = "/create-session";
    // Session operations
    public static final String SESSION_BY_ID = "/{sessionId}";
    public static final String RENAME_SESSION = "/{sessionId}/rename";
    public static final String FAVORITE_SESSION = "/{sessionId}/favorite";
    public static final String GET_ALL_SESSIONS = "/get-all-sessions";
    public static final String SUCCESS = "success";

    // ===== Message APIs =====
    public static final String ADD_MESSAGE = "/{sessionId}/add-messages";
    public static final String GET_MESSAGES = "/{sessionId}/get-messages";
}

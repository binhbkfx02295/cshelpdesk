package com.binhbkfx02295.cshelpdesk.websocket.dto;


public record NotificationDTO<T>(
        String entity,        // "TICKET", "MESSAGE", "EMPLOYEE"
        String action,        // "CREATED", "UPDATED", "DELETED"
        T data                // DTO payload to stringify at client side
) { }
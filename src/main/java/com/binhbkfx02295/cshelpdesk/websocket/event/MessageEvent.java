package com.binhbkfx02295.cshelpdesk.websocket.event;

import com.binhbkfx02295.cshelpdesk.dto.MessageEventDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageEvent {
    private final MessageEventDTO message;
}
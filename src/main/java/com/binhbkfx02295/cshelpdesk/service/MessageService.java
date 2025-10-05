package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.MessageDTO;

import java.util.List;

public interface MessageService {
    MessageDTO addMessage(MessageDTO messageDTO);
    List<MessageDTO> getMessagesByTicketId(int ticketId);
}

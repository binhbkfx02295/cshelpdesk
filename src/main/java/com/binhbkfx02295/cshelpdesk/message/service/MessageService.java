package com.binhbkfx02295.cshelpdesk.message.service;

import com.binhbkfx02295.cshelpdesk.message.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface MessageService {

    String MSG_RETRIEVE_MESSAGES_OK = "Truy vấn tin nhắn trò chuyện thành công";
    String MSG_RETRIEVE_MESSAGES_FAILED = "Truy vấn tin nhắn trò chuyện thất bại";
    String MSG_ADD_MESSAGE_FAILED = "Lưu tin nhắn thất bại";
    String MSG_ADD_MESSAGE_SUCCESS = "Lưu tin nhắn thành công";

    APIResultSet<MessageDTO> addMessage(MessageDTO messageDTO);
    APIResultSet<List<MessageDTO>> getMessagesByTicketId(int ticketId);

}

package com.binhbkfx02295.cshelpdesk.message.controller;

import com.binhbkfx02295.cshelpdesk.message.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.message.service.MessageService;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResponseEntityHelper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessagesByTicket(
            @RequestParam("ticketId") int ticketId
    ) {
        return ResponseEntity.ok(messageService.getMessagesByTicketId(ticketId));
    }

    @PostMapping
    public ResponseEntity<MessageDTO> addMessage(
            @RequestBody MessageDTO messageDTO
    ) {
        return ResponseEntity.ok(messageService.addMessage(messageDTO));
    }
}

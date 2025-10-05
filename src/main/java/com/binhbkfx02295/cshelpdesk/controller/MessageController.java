package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.service.MessageService;
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

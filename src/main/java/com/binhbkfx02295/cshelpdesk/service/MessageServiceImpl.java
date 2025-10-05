package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.entity.Message;
import com.binhbkfx02295.cshelpdesk.dto.MessageMapper;
import com.binhbkfx02295.cshelpdesk.repository.MessageRepository;
import com.binhbkfx02295.cshelpdesk.repository.TicketRepository;
import com.binhbkfx02295.cshelpdesk.websocket.event.MessageEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final TicketRepository ticketRepository;
    private final MessageMapper mapper;
    private final ApplicationEventPublisher publisher;
    private final EntityManager entityManager;

    @Override
    public MessageDTO addMessage(MessageDTO messageDTO) {
        Message saved = messageRepository.save(mapper.toEntity(messageDTO));
        publisher.publishEvent(new MessageEvent(mapper.toEventDTO(saved)));
        return mapper.toDTO(saved);
    }

    @Override
    public List<MessageDTO> getMessagesByTicketId(int ticketId) {
        if (!ticketRepository.existsById(ticketId))
            throw new EntityNotFoundException("Loi ticket khong ton tai");

        return messageRepository.findByTicket_Id(ticketId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

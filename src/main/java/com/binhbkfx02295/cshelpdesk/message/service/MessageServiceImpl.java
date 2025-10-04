package com.binhbkfx02295.cshelpdesk.message.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.message.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.message.entity.Message;
import com.binhbkfx02295.cshelpdesk.message.mapper.MessageMapper;
import com.binhbkfx02295.cshelpdesk.message.repository.MessageRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.repository.TicketRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.websocket.event.MessageEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final TicketRepository ticketRepository;
    private final MasterDataCache cache;
    private final MessageMapper mapper;
    private final ApplicationEventPublisher publisher;
    private final EntityManager entityManager;

    @Override
    public MessageDTO addMessage(MessageDTO messageDTO) {
        Message saved = messageRepository.save(mapper.toEntity(messageDTO));
        publisher.publishEvent(new MessageEvent(mapper.toEventDTO(cache.getMessage(saved.getId()))));
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

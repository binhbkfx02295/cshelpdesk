package com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.facebookuser.entity.FacebookUser;
import com.binhbkfx02295.cshelpdesk.facebookuser.repository.FacebookUserRepository;
import com.binhbkfx02295.cshelpdesk.message.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.message.entity.Message;
import com.binhbkfx02295.cshelpdesk.message.repository.MessageRepository;
import com.binhbkfx02295.cshelpdesk.message.service.MessageServiceImpl;
import com.binhbkfx02295.cshelpdesk.openai.model.GPTResult;
import com.binhbkfx02295.cshelpdesk.openai.service.GPTTicketService;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.entity.Category;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.repository.CategoryRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.emotion.repository.EmotionRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.note.dto.NoteDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.note.mapper.NoteMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.entity.ProgressStatus;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.repository.ProgressStatusRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.repository.SatisfactionRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.*;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.repository.TicketRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.mapper.TicketMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.spec.TicketSpecification;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import com.binhbkfx02295.cshelpdesk.websocket.event.TicketAssignedEvent;
import com.binhbkfx02295.cshelpdesk.websocket.event.TicketEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;
    private final MessageServiceImpl messageService;
    private final GPTTicketService gptService;
    private final CategoryRepository categoryRepository;
    private final EmotionRepository emotionRepository;
    private final SatisfactionRepository satisfactionRepository;
    private final ProgressStatusRepository progressStatusRepository;
    private final FacebookUserRepository facebookUserRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplicationEventPublisher publisher;
    private final MessageRepository messageRepository;
    private final NoteMapper noteMapper;


    @Override
    public TicketDetailDTO createTicket(TicketDetailDTO dto) {
        if (dto.getProgressStatus() == null)
            throw new IllegalArgumentException("Loi thieu trang thai");

        if (dto.getFacebookUser() == null)
            throw new IllegalArgumentException("Loi thieu nguoi dung");


        Ticket ticket = new Ticket();
        ticket.setCreatedAt(Timestamp.from(Instant.now()));

        ticket.setFacebookUser(getFacebookUser(dto));

        ticket.setCategory(dto.getCategory() == null ? null :
                getCategory(dto));

        ticket.setAssignee(dto.getAssignee() == null ? null :
                getEmployee(dto));

        ticket.setProgressStatus(dto.getProgressStatus() == null ? null :
                getProgressStatus(dto));

        ticket.setTitle(dto.getTitle());
        Ticket saved = ticketRepository.save(ticket);
        publisher.publishEvent(new TicketEvent(mapper.toDashboardDTO(saved), TicketEvent.Action.CREATED));
        return mapper.toDetailDTO(saved);
    }

    private FacebookUser getFacebookUser(TicketDetailDTO dto) {
        return facebookUserRepository.get(dto.getFacebookUser().getFacebookId());
    }

    private ProgressStatus getProgressStatus(TicketDetailDTO dto) {
        return progressStatusRepository.findById(dto.getProgressStatus().getId())
                .orElseThrow(() -> new EntityNotFoundException("Progress status khong ton tai"));
    }

    private Employee getEmployee(TicketDetailDTO dto) {
        return employeeRepository.findById(dto.getAssignee().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User khong ton tai"));
    }

    private Category getCategory(TicketDetailDTO dto) {
        return categoryRepository.findById(dto.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category khong ton tai"));
    }

    @Override
    public TicketDetailDTO updateTicket(int id, TicketDetailDTO dto) {
        Ticket ticket = ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Lỗi ticket không tồn tại"));

        if (dto.getProgressStatus() == null)
            throw new IllegalArgumentException("Loi thieu trang thai");

        if (dto.getFacebookUser() == null)
            throw new IllegalArgumentException("Loi thieu nguoi dung");


        ticket.setTitle(dto.getTitle());
        ticket.setAssignee(dto.getAssignee() == null ? null :
                getEmployee(dto));

        ticket.setProgressStatus(getProgressStatus(dto));

        if (ticket.getProgressStatus().getId() == 3 &&
                (ticket.getFirstResponseRate() == null ||
                        ticket.getOverallResponseRate() == null ||
                        ticket.getResolutionRate() == null)) {
            ticket.setClosedAt(new Timestamp(System.currentTimeMillis()));
            calculateKPI(ticket);

            Ticket finalTicket = ticket;
            List<Message> messages = messageRepository.findByTicket_Id(ticket.getId())
                    .stream()
                    .filter(message -> message.getTicket().getId() == finalTicket.getId())
                    .toList();

            GPTResult gptResult = gptService.analyze(messages);

            ticket.setCategory(categoryRepository.getReferenceById(gptResult.getCategoryId()));
            ticket.setSatisfaction(satisfactionRepository.getReferenceById(gptResult.getSatisfactionId()));
            ticket.setEmotion(emotionRepository.getReferenceById(gptResult.getEmotionId()));
            ticket.setPrice(gptResult.getPrice());
            ticket.setGptModelUsed(gptResult.getGptModelused());
            ticket.setTokenUsed(gptResult.getTokenUsed());

        }
        ticket = ticketRepository.save(ticket);

        if (ticket.getProgressStatus().getId() == 3) {
            publisher.publishEvent(new TicketEvent(mapper.toDashboardDTO(ticket), TicketEvent.Action.CLOSED));
        }
        else {
            publisher.publishEvent(new TicketEvent(mapper.toDashboardDTO(ticket), TicketEvent.Action.UPDATED));
        }
        return mapper.toDetailDTO(ticket);
    }

    @Override
    public TicketDetailDTO getTicketById(int id) {
        return mapper.toDetailDTO(ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Khong ton tai ticket so " + id)));
    }

    @Override
    public void addTagToTicket(int ticketId, int hashtagId) {
        //TODO:
        unsupported();
    }

    @Override
    public void removeTagFromTicket(int ticketId, int tagId) {
        //TODO:
        unsupported();
    }

    @Override
    public TicketDetailDTO findExistingTicket(String facebookId) {

        return mapper.toDetailDTO(ticketRepository
                .findFirstByFacebookUser_FacebookIdOrderByCreatedAtDesc(facebookId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ticket cua Facebook user nay")));
    }

    @Override
    public List<TicketListDTO> findAllByFacebookUserId(String facebookId) {
        return ticketRepository.findAllByFacebookUser_FacebookId(facebookId)
                .stream().map(mapper::toListDTO).toList();
    }

    @Override
    public void addNoteToTicket(int ticketId, NoteDTO noteDTO) {
        //TODO:
        unsupported();
    }



    @Override
    public void deleteNoteFromTicket(int ticketId, int noteId) {
        //TODO:
        unsupported();
    }

    @Override
    public Set<NoteDTO> getNotes(int ticketId) {
        return ticketRepository.findByIdWithDetails(ticketId)
                .map(ticket -> ticket.getNotes()
                        .stream()
                        .map(noteMapper::toDTO)
                        .collect(Collectors.toSet()))
                .orElseThrow(() -> new EntityNotFoundException("Ticket khong ton tai"));
    }

    @Override
    public PaginationResponse<TicketListDTO> searchTickets(TicketSearchCriteria criteria,
                                                           Pageable pageable) {
        var spec = TicketSpecification.build(criteria);
        var page = ticketRepository.findAll(spec, pageable);

        return new PaginationResponse<>(
                page.getContent().stream()
                        .map(mapper::toListDTO)
                        .collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }


    @Override
    public void deleteById(int ticketId) {
        ticketRepository.deleteById(ticketId);
    }



    @Override
    public List<TicketDashboardDTO> getForDashboard(String username) {
        Employee employee = employeeRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay nhan vien"));

        Timestamp start = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1));

        //TODO: optimize
        List<Ticket> tickets = ticketRepository.findOpeningOrToday(start, end);

        if (employee.getUserGroup().getCode().equalsIgnoreCase("staff")) {
            tickets = tickets
                    .stream()
                    .filter(ticket ->
                            ticket.getAssignee() == null ||
                                    ticket.getAssignee().getUsername().equalsIgnoreCase(username))
                    .toList();
        }

        return tickets.stream().map(mapper::toDashboardDTO).toList();
    }

    private void calculateKPI(Ticket existing) {

        long firstResponseRate = 0;
        long overallResponseRate = 0;
        long resolutionRate = (existing.getClosedAt().getTime() - existing.getCreatedAt().getTime())/1000;

        List<MessageDTO> messageList = messageService.getMessagesByTicketId(existing.getId());
        messageList.sort((o1, o2) -> o1.getId() - o2.getId());

        // Tính first response time
        log.info("bat dau tinh");
        for (int i = 0; i < messageList.size(); i++) {
            MessageDTO current = messageList.get(i);
            if (i==0 && current.isSenderEmployee()) {
                log.info("o day ne");
                break;
            }

            if (current.isSenderEmployee()) {
                // tìm tin nhắn trước đó là của khách hàng
                MessageDTO customerMessage = messageList.get(i-1);
                log.info(current.toString());
                log.info(customerMessage.toString());
                firstResponseRate = (current.getTimestamp().getTime() - customerMessage.getTimestamp().getTime()) / 1000;
                break;
            }
        }

        // Tính average response time giữa mỗi lần khách nhắn và nhân viên trả lời
        List<Long> responseTimes = new ArrayList<>();
        MessageDTO lastCustomerMsg = null;
        for (MessageDTO msg : messageList) {
            if (!msg.isSenderEmployee()) {
                lastCustomerMsg = msg;
            } else if (lastCustomerMsg != null) {
                long respTime = (msg.getTimestamp().getTime() - lastCustomerMsg.getTimestamp().getTime()) / 1000;
                if (respTime > 0) {
                    responseTimes.add(respTime);
                }
                lastCustomerMsg = null;
            }
        }

        if (lastCustomerMsg != null) {
            long respTime = (existing.getClosedAt().getTime() - lastCustomerMsg.getTimestamp().getTime())/1000;
            if (respTime > 0) {
                responseTimes.add(respTime);
            }
        }

        overallResponseRate = responseTimes.isEmpty()
                ? 0
                : responseTimes.stream().mapToLong(Long::longValue).sum() / responseTimes.size();

        existing.setFirstResponseRate(firstResponseRate);
        existing.setOverallResponseRate(overallResponseRate);
        existing.setResolutionRate(resolutionRate);
        log.info("Ticket {} closed, tinh KPI thanh cong: {} {} {}", existing.getId(), firstResponseRate,
                overallResponseRate,
                resolutionRate);
    }

    @Override
    public List<TicketVolumeReportDTO> searchTicketsForVolumeReport(Timestamp fromTime, Timestamp toTime) {
        return ticketRepository.findTicketsForHourlyReport(fromTime, toTime);
    }

    @Override
    public TicketDetailDTO assignTicket(int id, TicketDetailDTO dto) {
        if (dto.getAssignee() == null)
            throw new IllegalArgumentException("Lỗi chưa gán assignee");

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loi ticket khong ton tai"));

        ticket.setAssignee(employeeRepository.findById(dto.getAssignee().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Loi khong tim thay employee")));

        ticket = ticketRepository.save(ticket);
        publisher.publishEvent(new TicketAssignedEvent(mapper.toDashboardDTO(ticket)));
        return mapper.toDetailDTO(ticket);
    }

    private static void unsupported() {
        throw new UnsupportedOperationException("Chua xay dung API");
    }


}


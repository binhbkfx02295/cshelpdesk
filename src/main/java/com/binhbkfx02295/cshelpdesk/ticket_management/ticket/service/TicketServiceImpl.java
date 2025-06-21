package com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.facebookuser.service.FacebookUserServiceImpl;
import com.binhbkfx02295.cshelpdesk.message.dto.MessageDTO;
import com.binhbkfx02295.cshelpdesk.message.entity.Message;
import com.binhbkfx02295.cshelpdesk.openai.model.GPTResult;
import com.binhbkfx02295.cshelpdesk.openai.service.GPTTicketService;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.repository.CategoryRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.emotion.repository.EmotionRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.note.entity.Note;
import com.binhbkfx02295.cshelpdesk.ticket_management.note.dto.NoteDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.note.repository.NoteRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.repository.SatisfactionRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.tag.dto.TagDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.*;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.repository.TicketRepository;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.mapper.TicketMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.spec.TicketSpecification;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import com.binhbkfx02295.cshelpdesk.websocket.event.TicketAssignedEvent;
import com.binhbkfx02295.cshelpdesk.websocket.event.TicketEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketServiceImpl implements TicketService {


    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;
    private final NoteRepository noteRepository;
    private final FacebookUserServiceImpl facebookUserService;
    private final MasterDataCache cache;
    private final GPTTicketService gptService;
    private final CategoryRepository categoryRepository;
    private final EmotionRepository emotionRepository;
    private final SatisfactionRepository satisfactionRepository;
    private final ApplicationEventPublisher publisher;
    private final EntityManager entityManager;


    @Override
    public APIResultSet<TicketDetailDTO> createTicket(TicketDetailDTO dto) {
        APIResultSet<TicketDetailDTO> result;
        try {
            APIResultSet<TicketDetailDTO> validationResult = validateTicketDTO(dto);
            if (validationResult.getHttpCode() != 200) {
                return validationResult;
            }

            if (dto.getFacebookUser() == null) {
                return  APIResultSet.badRequest(MSG_ERROR_VALIDATION_NO_FACEBOOK_ID);
            }
            APIResultSet<Void> validateFacebookUser = facebookUserService.existsById(dto.getFacebookUser().getFacebookId());
            if (validateFacebookUser.getHttpCode() != 200) {
                return APIResultSet.badRequest(validateFacebookUser.getMessage());
            }

            Ticket ticket = mapper.toEntity(dto);
            if (ticket.getCreatedAt() == null) {
                ticket.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }
            Ticket saved = ticketRepository.save(ticket);
            entityManager.flush();
            entityManager.clear();
            cache.updateOpeningTickets();
            result = APIResultSet.ok(MSG_SUCCESS_CREATE_TICKET, mapper.toDetailDTO(cache.getTicket(saved.getId())));
            publisher.publishEvent(new TicketEvent(mapper.toDashboardDTO(cache.getTicket(saved.getId())), TicketEvent.Action.CREATED));

        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<TicketDetailDTO> updateTicket(int id, TicketDetailDTO dto) {
        APIResultSet<TicketDetailDTO> result;
        try {
            Optional<Ticket> existingOpt = ticketRepository.findByIdWithDetails(id);
            if (existingOpt.isEmpty()) {
                return APIResultSet.notFound(MSG_ERROR_TICKET_NOT_EXISTS);
            }

            APIResultSet<TicketDetailDTO> validationResult = validateTicketDTO(dto);
            if (validationResult.getHttpCode() != 200) {
                return validationResult;
            }

            Ticket existing = existingOpt.get();
            mapper.mergeToEntity(dto, existing);

            if (existing.getProgressStatus().getId() == 3 &&
                    (existing.getFirstResponseRate() == null ||
                            existing.getOverallResponseRate() == null ||
                            existing.getResolutionRate() == null)) {
                existing.setClosedAt(new Timestamp(System.currentTimeMillis()));

                List<Message> messages = cache.getAllMessages().values().stream().filter(message ->
                        message.getTicket().getId() == existing.getId()).toList();
                if (!messages.isEmpty()) {
                    messages = existing.getMessages();
                }
                GPTResult gptResult = gptService.analyze(messages);

                existing.setCategory(categoryRepository.getReferenceById(gptResult.getCategoryId()));
                existing.setSatisfaction(satisfactionRepository.getReferenceById(gptResult.getSatisfactionId()));
                existing.setEmotion(emotionRepository.getReferenceById(gptResult.getEmotionId()));
                existing.setPrice(gptResult.getPrice());
                existing.setGptModelUsed(gptResult.getGptModelused());
                existing.setTokenUsed(gptResult.getTokenUsed());

            }
            existing.setLastUpdateAt(new Timestamp(System.currentTimeMillis()));
            Ticket saved = ticketRepository.save(existing);
            entityManager.flush();
            entityManager.clear();
            cache.updateOpeningTickets();
            if (saved.getProgressStatus().getId() == 3) {
                Ticket temp = cache.getTicket(existing.getId());
                if (temp == null) {
                    temp = ticketRepository.findByIdWithDetails(existing.getId()).get();
                }
                publisher.publishEvent(new TicketEvent(mapper.toDashboardDTO(temp), TicketEvent.Action.CLOSED));
            }
            else {
                publisher.publishEvent(new TicketEvent(mapper.toDashboardDTO(cache.getTicket(existing.getId())), TicketEvent.Action.UPDATED));
            }
            Ticket temp = cache.getTicket(existing.getId());
            if (temp == null) {
                temp = ticketRepository.findByIdWithDetails(existing.getId()).get();
            }
            result = APIResultSet.ok(MSG_SUCCESS_UPDATE_TICKET, mapper.toDetailDTO(temp));
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<TicketDetailDTO> getTicketById(int id) {
        try {
            return ticketRepository.findByIdWithDetails(id)
                    .map(ticket ->APIResultSet.ok(MSG_SUCCESS_GET_TICKET, mapper.toDetailDTO(ticket)))
                    .orElseGet(() -> APIResultSet.notFound(MSG_ERROR_GET_TICKET_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError();
        }
    }

    @Override
    public APIResultSet<Void> addTagToTicket(int ticketId, int hashtagId) {
        return null;
    }

    @Override
    public APIResultSet<Void> removeTagFromTicket(int ticketId, int tagId) {
        return null;
    }

    @Override
    public APIResultSet<TicketDetailDTO> findExistingTicket(String facebookId) {
        APIResultSet<TicketDetailDTO> result;
        try {
            Optional<Ticket> ticket = ticketRepository
                    .findFirstByFacebookUser_FacebookIdOrderByCreatedAtDesc(facebookId);
            result = ticket
                    .map(value -> APIResultSet.ok(MSG_ERROR_TICKET_FOUND, mapper.toDetailDTO(value)))
                    .orElseGet(() -> APIResultSet.notFound(MSG_ERROR_TICKET_NOT_EXISTS));
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<List<TicketListDTO>> findAllByFacebookUserId(String facebookId) {
        APIResultSet<List<TicketListDTO>> result;
        try {
            List<Ticket> tickets = ticketRepository.findAllByFacebookUser_FacebookId(facebookId);
            result = APIResultSet.ok(MSG_SUCCESS_GET_ALL_TICKETS, tickets.stream().map(mapper::toListDTO).toList());
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<Void> addNoteToTicket(int ticketId, NoteDTO noteDTO) {
        return null;
    }

    @Override
    public APIResultSet<Void> deleteNoteFromTicket(int ticketId, int noteId) {
        return null;
    }

    @Override
    public APIResultSet<Set<NoteDTO>> getNotes(int ticketId) {
        APIResultSet<Set<NoteDTO>> result;
        try {
            if (!noteRepository.existsByTicket_Id(ticketId)) {
                result = APIResultSet.notFound("Không tìm thấy ticket.");
            } else {
                result = APIResultSet.ok("Danh sách notes", noteRepository.findAllByTicket_Id(ticketId).stream()
                        .map(Note::toDTO).collect(Collectors.toSet()));
            }
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError("Lỗi server lấy tất cả note của ticket");
        }
        return result;
    }

    @Override
    public APIResultSet<PaginationResponse<TicketListDTO>> searchTickets(TicketSearchCriteria criteria, Pageable pageable) {
        APIResultSet<PaginationResponse<TicketListDTO>> result;
        try {
            var spec = TicketSpecification.build(criteria);
            var page = ticketRepository.findAll(spec, pageable);
            List<TicketListDTO> dtoList = page.getContent().stream()
                    .map(mapper::toListDTO)
                    .collect(Collectors.toList());
            PaginationResponse<TicketListDTO> pagination = new PaginationResponse<>(
                    dtoList,
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            );
            if (dtoList.isEmpty()) {
                result = APIResultSet.notFound(MSG_ERROR_SEARCH_TICKETS_NOT_FOUND);
            } else {
                result = APIResultSet.ok(MSG_SUCCESS_SEARCH_TICKETS_FOUND, pagination);
            }
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }



    private APIResultSet<TicketDetailDTO> validateTicketDTO(TicketDetailDTO dto) {

        if (dto.getAssignee() != null &&
                cache.getEmployee(dto.getAssignee().getUsername()) == null) {
            return APIResultSet.badRequest(MSG_ERROR_VALIDATION_EMPLOYEE_NOT_EXISTS);
        }

        if (dto.getProgressStatus() == null) {
            return APIResultSet.badRequest(MSG_ERROR_VALIDATION_NO_PROGRESS_STATUS);
        } else if (cache.getProgress(dto.getProgressStatus().getId()) == null){
            return APIResultSet.badRequest(MSG_ERROR_VALIDATION_PROGRESS_STATUS_NOT_EXISTS);
        }

        if (dto.getCategory() != null &&
                cache.getCategory(dto.getCategory().getId()) == null) {
            return APIResultSet.badRequest(MSG_ERROR_VALIDATION_CATEGORY_NOT_EXISTS);
        }

        if (dto.getEmotion() != null &&
                cache.getEmotion(dto.getEmotion().getId()) == null) {
            return APIResultSet.badRequest(MSG_ERROR_VALIDATION_EMOTION_NOT_EXISTS);
        }

        if (dto.getSatisfaction() != null &&
                cache.getSatisfaction(dto.getSatisfaction().getId()) == null) {
            return APIResultSet.badRequest(MSG_ERROR_VALIDATION_SATISFACTION_NOT_EXISTS);
        }

        if (dto.getTags() != null) {
            for (TagDTO tag : dto.getTags()) {
                if (cache.getTag(tag.getId()) == null) {
                    return APIResultSet.badRequest(MSG_ERROR_VALIDATION_TAG_NOT_EXISTS);
                }
            }
        }

        return APIResultSet.ok("Validation passed", null);
    }

    @Override
    public APIResultSet<Void> deleteById(int ticketId) {
        APIResultSet<Void> result;
        try {
            if (!ticketRepository.existsById(ticketId)) {
                result = APIResultSet.notFound(MSG_ERROR_TICKET_NOT_EXISTS);
            } else {
                ticketRepository.deleteById(ticketId);
                cache.updateOpeningTickets();
                cache.getDashboardTickets().remove(ticketId);
                result = APIResultSet.ok("Xóa ticket thành công.", null);
            }
        }
        catch(Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }



    @Override
    public APIResultSet<List<TicketDashboardDTO>> getForDashboard(String username) {
        APIResultSet<List<TicketDashboardDTO>> result;
        try {
            List<Ticket> tickets = cache.getDashboardTickets().values().stream().toList();
            Employee employee = cache.getEmployee(username);
            if (employee != null && employee.getUserGroup().getCode().equalsIgnoreCase("staff")) {
                tickets = tickets.stream().filter(ticket ->
                        ticket.getAssignee() == null || ticket.getAssignee().getUsername().equalsIgnoreCase(username)).toList();
            }
            result = APIResultSet.ok(MSG_SUCCESS_SEARCH_TICKETS_FOUND,
                    tickets.stream().map(mapper::toDashboardDTO).toList());
        } catch(Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public void calculateKPI(TicketReportDTO ticket) {

        long firstResponseRate = 0;
        long overallResponseRate = 0;
        long resolutionRate = (ticket.getClosedAt() - ticket.getCreatedAt())/1000;

        List<MessageDTO> messageList = ticket.getMessages();
        messageList.sort((o1, o2) -> o1.getId() - o2.getId());
        for (int i = 0; i < messageList.size(); i++) {
            MessageDTO current = messageList.get(i);
            if (i==0 && current.isSenderEmployee()) {
                break;
            }

            if (current.isSenderEmployee()) {
                MessageDTO customerMessage = messageList.get(i-1);
                firstResponseRate = (current.getTimestamp().getTime() - customerMessage.getTimestamp().getTime()) / 1000;
                break;
            }
        }

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
            long respTime = (ticket.getClosedAt() - lastCustomerMsg.getTimestamp().getTime())/1000;
            if (respTime > 0) {
                responseTimes.add(respTime);
            }
        }
        overallResponseRate = responseTimes.isEmpty()
                ? 0
                : responseTimes.stream().mapToLong(Long::longValue).sum() / responseTimes.size();

        ticket.setFirstResponseTime(firstResponseRate);
        ticket.setAvgResponseTime(overallResponseRate);
        ticket.setResolutionTime(resolutionRate);
    }


    @Override
    public APIResultSet<List<TicketReportDTO>> findResolvedByMonth(Timestamp startOfMonth, Timestamp endOfMonth) {
        APIResultSet<List<TicketReportDTO>> result;
        try {
            List<Ticket> tickets = ticketRepository.findResolvedByMonth(startOfMonth, endOfMonth);
            result = APIResultSet.ok("OK", tickets.stream().map(mapper::toReportDTO).toList());
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<List<TicketVolumeReportDTO>> searchTicketsForVolumeReport(Timestamp fromTime, Timestamp toTime) {
        APIResultSet<List<TicketVolumeReportDTO>> result;
        try {
            List<TicketVolumeReportDTO> tickets = ticketRepository.findTicketsForHourlyReport(fromTime, toTime);
            result = APIResultSet.ok(MSG_SUCCESS_SEARCH_TICKETS_FOUND, tickets);
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public void assignTicket(int id, TicketDetailDTO dto) {
        APIResultSet<TicketDetailDTO> result;
        try {
            if (dto.getAssignee() == null) {
                result = APIResultSet.badRequest(MSG_ERROR_ASSIGN_NO_EMPLOYEE_CHOSEN);
            } else {
                result = updateTicket(id, dto);
                publisher.publishEvent(new TicketAssignedEvent(mapper.toDashboardDTO(cache.getTicket(id))));
            }
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }

    }

    @Override
    public APIResultSet<List<TicketReportDTO>> getForEvaluation() {
        APIResultSet<List<TicketReportDTO>> result;
        try {
            List<Ticket> tickets = ticketRepository.findResolvedWithMessages();
            result = APIResultSet.ok(
                    MSG_SUCCESS_SEARCH_TICKETS_FOUND,
                    tickets
                            .stream()
                            .map(mapper::toReportDTO)
                            .toList());
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }
}


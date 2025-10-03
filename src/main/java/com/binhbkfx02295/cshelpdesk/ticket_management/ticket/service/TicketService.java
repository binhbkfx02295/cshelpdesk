package com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service;

import com.binhbkfx02295.cshelpdesk.ticket_management.note.dto.NoteDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.*;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


public interface TicketService {

    TicketDetailDTO createTicket(TicketDetailDTO dto);
    TicketDetailDTO updateTicket(int id, TicketDetailDTO dto);
    TicketDetailDTO getTicketById(int id);
    void addTagToTicket(int ticketId, int hashtagId);
    void removeTagFromTicket(int ticketId, int hashtagId);
    PaginationResponse<TicketListDTO> searchTickets(TicketSearchCriteria criteria, Pageable pageable);
    TicketDetailDTO findExistingTicket(String facebookId);
    List<TicketListDTO> findAllByFacebookUserId(String facebookId);
    void addNoteToTicket(int ticketId, NoteDTO note);
    void deleteNoteFromTicket(int ticketId, int noteId);
    Set<NoteDTO> getNotes(int ticketId);
    void deleteById(int ticketId);
    List<TicketDashboardDTO> getForDashboard(String username);
    List<TicketVolumeReportDTO> searchTicketsForVolumeReport(Timestamp fromTime, Timestamp toTime);
    TicketDetailDTO assignTicket(int id, TicketDetailDTO dto);
}

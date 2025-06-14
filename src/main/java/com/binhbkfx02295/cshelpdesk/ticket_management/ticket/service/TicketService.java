package com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service;

import com.binhbkfx02295.cshelpdesk.ticket_management.note.dto.NoteDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.*;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;


public interface TicketService {

    APIResultSet<TicketDetailDTO> createTicket(TicketDetailDTO dto);
    APIResultSet<TicketDetailDTO> updateTicket(int id, TicketDetailDTO dto);
    APIResultSet<TicketDetailDTO> getTicketById(int id);
    APIResultSet<Void> addTagToTicket(int ticketId, int hashtagId);
    APIResultSet<Void> removeTagFromTicket(int ticketId, int hashtagId);
    APIResultSet<PaginationResponse<TicketListDTO>> searchTickets(TicketSearchCriteria criteria, Pageable pageable);
    APIResultSet<TicketDetailDTO> findExistingTicket(String facebookId);
    APIResultSet<List<TicketListDTO>> findAllByFacebookUserId(String facebookId);
    APIResultSet<Void> addNoteToTicket(int ticketId, NoteDTO note);
    APIResultSet<Void> deleteNoteFromTicket(int ticketId, int noteId);
    APIResultSet<Set<NoteDTO>> getNotes(int ticketId);
    APIResultSet<Void> deleteById(int ticketId);
    APIResultSet<List<TicketDashboardDTO>> getForDashboard(String username);
    APIResultSet<List<TicketVolumeReportDTO>> searchTicketsForVolumeReport(Timestamp fromTime, Timestamp toTime);
    APIResultSet<TicketDetailDTO> assignTicket(int id, TicketDetailDTO dto);
    APIResultSet<List<TicketReportDTO>> getForEvaluation();
    void calculateKPI(TicketReportDTO ticket);
    APIResultSet<List<TicketReportDTO>> findResolvedByMonth(Timestamp startOfMonth, Timestamp endOfMonth);
}

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

    String MSG_ERROR_VALIDATION_NO_FACEBOOK_ID = "Lỗi thiếu Facebook User ID";
    String MSG_ERROR_VALIDATION_EMPLOYEE_NOT_EXISTS = "Lỗi nhân viên không tồn tại";
    String MSG_ERROR_VALIDATION_NO_PROGRESS_STATUS = "Lỗi thiếu trình trạng xử lý";
    String MSG_ERROR_VALIDATION_PROGRESS_STATUS_NOT_EXISTS = "Lỗi tình trạng xử lý không tồn tại";
    String MSG_ERROR_VALIDATION_CATEGORY_NOT_EXISTS = "Lỗi mã phân loại không tồn tại.";
    String MSG_ERROR_VALIDATION_EMOTION_NOT_EXISTS = "Lỗi mã cảm xúc không tồn tại.";
    String MSG_ERROR_VALIDATION_SATISFACTION_NOT_EXISTS = "Lỗi mã hài lòng không tồn tại.";
    String MSG_ERROR_VALIDATION_TAG_NOT_EXISTS = "Lỗi tag không tồn tại.";
    String MSG_SUCCESS_CREATE_TICKET = "Khởi tạo ticket thành công";
    String MSG_SUCCESS_UPDATE_TICKET = "Cập nhật ticket thành công";
    String MSG_ERROR_TICKET_NOT_EXISTS = "Lỗi ticket không tồn tại";
    String MSG_ERROR_TICKET_FOUND = "Đã tìm thấy ticket mới nhất";
    String MSG_SUCCESS_GET_ALL_TICKETS = "Truy vấn tất cả ticket thành công.";
    String MSG_ERROR_SEARCH_TICKETS_NOT_FOUND = "Không tìm thấy kết quả truy vấn.";
    String MSG_SUCCESS_SEARCH_TICKETS_FOUND = "Truy vấn tickets thành công.";
    String MSG_ERROR_ASSIGN_NO_EMPLOYEE_CHOSEN = "Lỗi chưa gán nhân viên";

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
    void assignTicket(int id, TicketDetailDTO dto);
    APIResultSet<List<TicketReportDTO>> getForEvaluation();
    void calculateKPI(TicketReportDTO ticket);
    APIResultSet<List<TicketReportDTO>> findResolvedByMonth(Timestamp startOfMonth, Timestamp endOfMonth);
}

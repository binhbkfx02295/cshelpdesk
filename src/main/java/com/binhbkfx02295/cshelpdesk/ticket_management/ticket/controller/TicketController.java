package com.binhbkfx02295.cshelpdesk.ticket_management.ticket.controller;

import com.binhbkfx02295.cshelpdesk.infrastructure.security.auth.UserPrincipal;
import com.binhbkfx02295.cshelpdesk.ticket_management.note.dto.NoteDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketListDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketDashboardDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketDetailDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketSearchCriteria;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service.TicketServiceImpl;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResponseEntityHelper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.TicketExcelExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.List;
@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketServiceImpl ticketService;

    @GetMapping()
    public ResponseEntity<TicketDetailDTO> getById(@RequestParam(value = "id") int id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PostMapping
    public ResponseEntity<TicketDetailDTO> create(@RequestBody TicketDetailDTO dto) {
        log.info(dto.toString());
        return ResponseEntity.ok(ticketService.createTicket(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDetailDTO> update(@PathVariable int id, @RequestBody TicketDetailDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicket(id, dto));
    }

    @GetMapping("/get-by-facebook-id")
    public ResponseEntity<List<TicketListDTO>> getByFacebookId(@RequestParam(value = "id") String id) {
        return ResponseEntity.ok(ticketService.findAllByFacebookUserId(id));
    }

    @PutMapping("/{ticketId}/note")
    public ResponseEntity<?> addNote(@PathVariable int ticketId, @RequestBody NoteDTO noteDto) {
        ticketService.addNoteToTicket(ticketId, noteDto);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{ticketId}/note/{noteId}")
    public ResponseEntity<?> removeNote(@PathVariable int ticketId, @PathVariable int noteId) {
        ticketService.deleteNoteFromTicket(ticketId, noteId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{ticketId}/note")
    public ResponseEntity<Set<NoteDTO>> getAllNotes(@PathVariable int ticketId) {
        return ResponseEntity.ok(ticketService.getNotes(ticketId));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PaginationResponse<TicketListDTO>> search(
            @ModelAttribute TicketSearchCriteria criteria,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ticketService.searchTickets(criteria, pageable));
    }

    @GetMapping(value = "/search-report")
    public ResponseEntity<PaginationResponse<TicketListDTO>> search(
            @ModelAttribute TicketSearchCriteria criteria) {
        return ResponseEntity.ok(ticketService.searchTickets(criteria, Pageable.unpaged()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<TicketDashboardDTO>> dashboard(
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        //TODO: replace with jwt stateless
        return ResponseEntity.ok(ticketService.getForDashboard(userPrincipal.getUsername()));
    }

    @PostMapping("/export-excel")
    public ResponseEntity<InputStreamResource> exportExcel(@RequestBody TicketSearchCriteria criteria) {
        try {
            // Lấy tất cả dữ liệu, không phân trang
            PaginationResponse<TicketListDTO> result = ticketService.searchTickets(criteria, Pageable.unpaged());
            List<TicketListDTO> tickets = result.getContent();

            ByteArrayInputStream in = TicketExcelExporter.exportToExcel(tickets);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=tickets.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(in));

        } catch (IOException e) {
            log.error("Lỗi xuất Excel: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
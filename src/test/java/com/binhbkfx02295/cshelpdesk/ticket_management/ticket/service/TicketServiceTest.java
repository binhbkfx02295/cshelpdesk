package com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service;


import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.usergroup.UserGroup;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserDetailDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserListDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.entity.FacebookUser;
import com.binhbkfx02295.cshelpdesk.facebookuser.repository.FacebookUserRepository;
import com.binhbkfx02295.cshelpdesk.facebookuser.service.FacebookUserService;
import com.binhbkfx02295.cshelpdesk.facebookuser.service.FacebookUserServiceImpl;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.dto.CategoryDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.entity.Category;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.dto.ProgressStatusDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.entity.ProgressStatus;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketDashboardDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketDetailDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketListDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketSearchCriteria;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.entity.Ticket;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.mapper.TicketMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.repository.TicketRepository;
import jakarta.persistence.EntityManager;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private static final TicketSearchCriteria BASE_CRITERIA = new TicketSearchCriteria();

    @Mock TicketRepository          ticketRepo;
    @Mock TicketMapper              mapper;
    @Mock MasterDataCache           cache;
    @Mock ApplicationEventPublisher publisher;
    @Mock
    FacebookUserServiceImpl facebookUserService;
    @Mock
    EntityManager entityManager;

    @InjectMocks TicketServiceImpl service;

    @BeforeAll
    static void beforeAll() {
        BASE_CRITERIA.setTitle(null);
        BASE_CRITERIA.setFacebookId(null);
        BASE_CRITERIA.setAssignee(null);
        BASE_CRITERIA.setCategory(0);
        BASE_CRITERIA.setProgressStatus(0);
        BASE_CRITERIA.setTag(null);
        BASE_CRITERIA.setFromTime(null);
        BASE_CRITERIA.setToTime(null);
    }

    @Test
    void searchticket_page0_size10_success() {
        TicketSearchCriteria criteria = copyCriteria();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Ticket ticket = new Ticket();
        ticket.setId(1);

        TicketListDTO dto = new TicketListDTO();
        dto.setId(1);

        when(ticketRepo.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(ticket), pageable, 1));
        when(mapper.toListDTO(ticket)).thenReturn(dto);

        APIResultSet<PaginationResponse<TicketListDTO>> rs =
                service.searchTickets(criteria, pageable);
        assertEquals(200, rs.getHttpCode());
        assertNotNull(rs.getData());
        assertEquals(1, rs.getData().getContent().size());
        verify(ticketRepo).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void searchTicket_page0_size100_success() {
        // thay đổi tiêu chí (ví dụ đặt progressId) để chứng minh criteria khác
        TicketSearchCriteria criteria = copyCriteria();
        criteria.setProgressStatus(2);                            // filter thêm

        Pageable pageable = PageRequest.of(0, 100);

        Ticket ticket = new Ticket();
        ticket.setId(2);

        TicketListDTO dto = new TicketListDTO();
        dto.setId(2);

        when(ticketRepo.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(ticket), pageable, 1));
        when(mapper.toListDTO(ticket)).thenReturn(dto);

        APIResultSet<PaginationResponse<TicketListDTO>> rs =
                service.searchTickets(criteria, pageable);
        assertEquals(200, rs.getHttpCode());
        assertEquals(1, rs.getData().getTotalElements());
    }

    @Test
    void searchTicket_page0_size10_noresult_notFoundCode() {
        TicketSearchCriteria criteria = copyCriteria();
        Pageable pageable = PageRequest.of(0, 10);

        when(ticketRepo.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(Page.empty(pageable));

        APIResultSet<PaginationResponse<TicketListDTO>> rs =
                service.searchTickets(criteria, pageable);
        assertEquals(404, rs.getHttpCode());
        assertNull(rs.getData());
    }

    private TicketSearchCriteria copyCriteria() {
        TicketSearchCriteria src = BASE_CRITERIA;

        TicketSearchCriteria c = new TicketSearchCriteria();
        c.setTitle(c.getTitle());
        c.setFacebookId(src.getFacebookId());
        c.setAssignee(src.getAssignee());
        c.setCategory(src.getCategory());
        c.setProgressStatus(src.getProgressStatus());
        c.setFromTime(src.getFromTime());
        c.setToTime(src.getToTime());
        return c;
    }

    @Test
    void createTicket_invalid_progress_status_failed() {
        TicketDetailDTO ticketDTO = mockTicket();
        ticketDTO.getProgressStatus().setId(4);

        when(cache.getEmployee(any())).thenReturn(new Employee());
        when(cache.getProgress(4)).thenReturn(null);

        APIResultSet<TicketDetailDTO> result = service.createTicket(ticketDTO);

        assertEquals(400, result.getHttpCode());
        assertEquals("Lỗi tình trạng xử lý không tồn tại", result.getMessage());
        assertNull(result.getData());
        verify(cache).getProgress(4);
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void createTicket_invalid_assignee() {
        TicketDetailDTO ticketDTO = mockTicket();
        ticketDTO.getAssignee().setUsername("binhbk99");
        when(cache.getEmployee("binhbk99")).thenReturn(null);
        APIResultSet<TicketDetailDTO> result = service.createTicket(ticketDTO);
        assertEquals(400, result.getHttpCode());
        assertEquals("Lỗi nhân viên không tồn tại", result.getMessage());
        assertNull(result.getData());
        verify(cache).getEmployee("binhbk99");
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void createTicket_invalid_facebookUser() {
        TicketDetailDTO ticketDTO = mockTicket();
        ticketDTO.getFacebookUser().setFacebookId("99999");
        ticketDTO.getAssignee().setUsername("binhbk");
        when(cache.getEmployee("binhbk")).thenReturn(new Employee());
        when(cache.getCategory(ticketDTO.getProgressStatus().getId())).thenReturn(new Category());
        when(cache.getProgress(ticketDTO.getProgressStatus().getId())).thenReturn(new ProgressStatus());
        when(facebookUserService.existsById(ticketDTO.getFacebookUser().getFacebookId()))
                .thenReturn(APIResultSet.notFound("Khách hàng không tồn tại"));
        APIResultSet<TicketDetailDTO> result = service.createTicket(ticketDTO);
        assertEquals(400, result.getHttpCode());
        assertEquals("Khách hàng không tồn tại", result.getMessage());
        assertNull(result.getData());
        verify(cache).getEmployee("binhbk");
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void createTicket_success() {
        TicketDetailDTO ticketDTO = mockTicket();
        when(cache.getProgress(anyInt())).thenReturn(new ProgressStatus());
        when(cache.getCategory(anyInt())).thenReturn(new Category());
        when(cache.getEmployee(anyString())).thenReturn(new Employee());
        when(facebookUserService.existsById(any())).thenReturn(APIResultSet.ok());
        when(mapper.toEntity(any(TicketDetailDTO.class))).thenReturn(new Ticket());
        when(ticketRepo.save(any(Ticket.class))).thenReturn(new Ticket());
        APIResultSet<TicketDetailDTO> result = service.createTicket(ticketDTO);

        assertEquals(200, result.getHttpCode());
        assertEquals("Khởi tạo ticket thành công", result.getMessage());
        verify(ticketRepo).save(any(Ticket.class));
    }

    TicketDetailDTO mockTicket() {
        TicketDetailDTO ticket = new TicketDetailDTO();
        ticket.setId(1);
        ProgressStatusDTO progressStatusDTO = new ProgressStatusDTO();
        progressStatusDTO.setId(1);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername("binhbk");

        FacebookUserListDTO facebookUserListDTO = new FacebookUserListDTO();
        facebookUserListDTO.setFacebookId("123");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);

        ticket.setFacebookUser(facebookUserListDTO);
        ticket.setProgressStatus(progressStatusDTO);
        ticket.setAssignee(employeeDTO);
        ticket.setCategory(categoryDTO);
        return ticket;
    }

    @Test
    void testUpdateTicket_invalidTicketId_failed() {
        when(ticketRepo.findByIdWithDetails(99999)).thenReturn(Optional.empty());
        APIResultSet<TicketDetailDTO> result = service.updateTicket(99999, new TicketDetailDTO());
        assertEquals(404, result.getHttpCode());
        assertEquals("Lỗi ticket không tồn tại", result.getMessage());
        verify(ticketRepo).findByIdWithDetails(99999);
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void testUpdateTicket_invalidProgressStatus_failed() {
        TicketDetailDTO ticket = mockTicket();
        when(ticketRepo.findByIdWithDetails(anyInt())).thenReturn(Optional.of(new Ticket()));
        when(cache.getEmployee(any())).thenReturn(new Employee());
        when(cache.getProgress(anyInt())).thenReturn(null);

        APIResultSet<TicketDetailDTO> result = service.updateTicket(ticket.getId(), ticket);
        System.out.println(result.getMessage());
        assertEquals(400, result.getHttpCode());
        assertEquals("Lỗi tình trạng xử lý không tồn tại", result.getMessage());
        verify(cache).getProgress(anyInt());
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void testUpdateTicket_invalidCategory_failed() {
        TicketDetailDTO ticket = mockTicket();
        when(ticketRepo.findByIdWithDetails(anyInt())).thenReturn(Optional.of(new Ticket()));

        when(cache.getEmployee(anyString())).thenReturn(new Employee());
        when(cache.getProgress(anyInt())).thenReturn(new ProgressStatus());
        when(cache.getCategory(anyInt())).thenReturn(null);

        APIResultSet<TicketDetailDTO> result = service.updateTicket(ticket.getId(), ticket);

        assertEquals(400, result.getHttpCode());
        assertEquals("Lỗi mã phân loại không tồn tại.", result.getMessage());
        verify(cache).getCategory(anyInt());
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void testUpdateTicket_invalidAssignee_failed() {
        TicketDetailDTO ticket = mockTicket();
        ticket.getAssignee().setUsername("binhbk99");
        when(ticketRepo.findByIdWithDetails(anyInt())).thenReturn(Optional.of(new Ticket()));
        when(cache.getEmployee(any())).thenReturn(null);

        APIResultSet<TicketDetailDTO> result = service.updateTicket(ticket.getId(), ticket);

        assertEquals(400, result.getHttpCode());
        assertEquals("Lỗi nhân viên không tồn tại", result.getMessage());
        verify(cache).getEmployee(any());
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void testUpdateTicket_success() {
        TicketDetailDTO dto = mockTicket();
        Ticket ticket = new Ticket();
        ticket.setProgressStatus(ProgressStatus.builder().id(1).build());
        when(ticketRepo.findByIdWithDetails(anyInt())).thenReturn(Optional.of(ticket));
        when(cache.getProgress(anyInt())).thenReturn(new ProgressStatus());
        when(cache.getCategory(anyInt())).thenReturn(new Category());
        when(cache.getEmployee(anyString())).thenReturn(new Employee());

        // Mapping và save như thông thường
        when(mapper.toDetailDTO(any(Ticket.class))).thenReturn(dto);
        when(ticketRepo.save(any(Ticket.class))).thenReturn(ticket);

        APIResultSet<TicketDetailDTO> result = service.updateTicket(dto.getId(), dto);
        System.out.println(result.getMessage());
        assertEquals(200, result.getHttpCode());
        assertNotNull(result.getData());
        verify(ticketRepo).save(any(Ticket.class));
        verify(mapper).toDetailDTO(any(Ticket.class));
    }

    @Test
    void testGetTicketById_ticket_id_not_exists_failed() {
        when(ticketRepo.findByIdWithDetails(11)).thenReturn(Optional.ofNullable(null));
        APIResultSet<TicketDetailDTO> result = service.getTicketById(11);
        assertEquals(404, result.getHttpCode());
        assertEquals("Không tìm thấy ticket", result.getMessage());
        verify(ticketRepo).findByIdWithDetails(11);
    }

    @Test
    void testGetTicketById_ticket_id_exists_success() {
        when(ticketRepo.findByIdWithDetails(1)).thenReturn(Optional.of(new Ticket()));
        when(mapper.toDetailDTO(any(Ticket.class))).thenReturn(new TicketDetailDTO());
        APIResultSet<TicketDetailDTO> result = service.getTicketById(1);
        assertEquals(200, result.getHttpCode());
        assertEquals("Truy vấn ticket thành công.", result.getMessage());
        verify(ticketRepo).findByIdWithDetails(1);
    }
    
    @Test
    void getForDashboard() {
        Employee employee = new Employee();
        UserGroup group = new UserGroup();
        group.setCode("staff");
        employee.setUserGroup(group);
        employee.setUsername("binhbk");

        when(cache.getDashboardTickets()).thenReturn(new HashMap<Integer,Ticket>());
        when(cache.getEmployee("binhbk")).thenReturn(employee);
        APIResultSet<List<TicketDashboardDTO>> result = service.getForDashboard("binhbk");
        assertEquals(200, result.getHttpCode());
        assertEquals("Truy vấn tickets thành công.", result.getMessage());
    }


}
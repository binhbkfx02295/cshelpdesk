package com.binhbkfx02295.cshelpdesk.webhook.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.config.FacebookAPIProperties;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.service.FacebookGraphAPIService;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserDetailDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.mapper.FacebookUserMapper;
import com.binhbkfx02295.cshelpdesk.facebookuser.service.FacebookUserService;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.message.service.MessageServiceImpl;
import com.binhbkfx02295.cshelpdesk.testutil.DummyEmployees;
import com.binhbkfx02295.cshelpdesk.testutil.TestEventBuilder;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.dto.ProgressStatusDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.mapper.ProgressStatusMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.dto.TicketDetailDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.mapper.TicketMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.ticket.service.TicketServiceImpl;
import com.binhbkfx02295.cshelpdesk.webhook.dto.WebHookEventDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class WebHookServiceTest {
    private static final String PAGE_ID     = "551419558065910";
    private static final String CUSTOMER_ID = "1111111111";

    @Mock FacebookUserService facebookUserService;
    @Mock FacebookGraphAPIService facebookGraphAPIService;
    @Mock TicketServiceImpl ticketService;
    @Mock MessageServiceImpl messageService;
    @Mock FacebookAPIProperties properties;
    @Mock FacebookUserMapper facebookUserMapper;
    @Mock ProgressStatusMapper progressStatusMapper;
    @Mock EmployeeMapper employeeMapper;
    @Mock MasterDataCache cache;
    @InjectMocks WebHookServiceImpl service;   // class under test

    @BeforeEach void init() {
        when(properties.getPageId()).thenReturn(PAGE_ID);
    }

    @Test void handleWebhook_senderCustomer_existed_getCustomer() {
        when(facebookUserService.get(CUSTOMER_ID))
                .thenReturn(Api.ok(facebookUser()));
        when(ticketService.findExistingTicket(CUSTOMER_ID))
                .thenReturn(Api.notFound());
        when(ticketService.createTicket(any()))
                .thenReturn(Api.ok(ticketWithNullAssignee()));

        service.handleWebhook(mockEventFromCustomer("Xin chào, tôi cần hỗ trợ"));
        verify(facebookUserService, never()).save(any(FacebookUserDetailDTO.class));
        verify(ticketService).createTicket(any());
    }

    /* 2 – Không có ticket → createTicket */
    @Test void handleWebhook_senderCustomer_ticketNull_createTicket() {
        when(facebookUserService.get(CUSTOMER_ID))
                .thenReturn(Api.ok(facebookUser()));
        when(ticketService.findExistingTicket(CUSTOMER_ID))
                .thenReturn(Api.notFound());
        when(ticketService.createTicket(any()))
                .thenReturn(Api.ok(ticketWithNullAssignee()));

        service.handleWebhook(mockEventFromCustomer("Xin chào, tôi cần hỗ trợ"));
        verify(ticketService).createTicket(any());
    }

    /* 3 – Đã có ticket → lưu message */
    @Test void handleWebhook_senderCustomer_ticketNotNull_message_saved() {
        TicketDetailDTO opened = ticketWithNullAssignee();
        ProgressStatusDTO p = new ProgressStatusDTO();
        p.setId(1);
        opened.setProgressStatus(p);
        when(facebookUserService.get(CUSTOMER_ID)).thenReturn(Api.ok(facebookUser()));
        when(ticketService.findExistingTicket(CUSTOMER_ID)).thenReturn(Api.ok(opened));
        service.handleWebhook(mockEventFromCustomer("Xin chào, tôi cần hỗ trợ"));

        verify(messageService).addMessage(any());
    }

    /* 4 – Ticket chưa có assignee & autoAssign = true  → assignTicket */
    @Test void handleWebhook_senderCustomer_ticketNotNull_assigneeNull_assignTicket() {
        TicketDetailDTO opened = ticketWithNullAssignee();
        when(facebookUserService.get(CUSTOMER_ID)).thenReturn(Api.ok(facebookUser()));
        when(ticketService.findExistingTicket(CUSTOMER_ID)).thenReturn(Api.ok(opened));
        when(cache.getAllEmployees()).thenReturn(DummyEmployees.onlineStaff(2));
        service.handleWebhook(mockEventFromCustomer("Xin chào, tôi cần hỗ trợ"));
        verify(ticketService).assignTicket(eq(opened.getId()), any());
    }

    /* 5 – Ticket chưa có assignee & autoAssign = false  → auto-reply */
    @Test void handleWebhook_senderCustomer_ticketNotNull_assigneeNull_autoReply() {
        TicketDetailDTO opened = ticketWithNullAssignee();
        when(facebookUserService.get(CUSTOMER_ID)).thenReturn(Api.ok(facebookUser()));
        when(ticketService.findExistingTicket(CUSTOMER_ID)).thenReturn(Api.ok(opened));
        when(cache.getAllEmployees()).thenReturn(DummyEmployees.noOnlineStaff());

        service.handleWebhook(mockEventFromCustomer("Xin chào, tôi cần hỗ trợ"));

        verify(facebookGraphAPIService).notifyNoAssignee(CUSTOMER_ID);
    }


    /* 8 – getOrCreateTicket được gọi khi ticket null */
    @Test void handleWebhook_notSenderEmployee_ticketNull_getOrCreateTicket() {
        when(facebookUserService.get(CUSTOMER_ID)).thenReturn(Api.ok(facebookUser()));
        when(ticketService.findExistingTicket(CUSTOMER_ID)).thenReturn(Api.notFound());
        when(ticketService.createTicket(any())).thenReturn(Api.ok(ticketWithAssignee()));
        service.handleWebhook(mockEventFromCustomer("Hello again"));
        verify(ticketService).createTicket(any());
    }


    private static WebHookEventDTO mockEventFromCustomer(String text) {
        return TestEventBuilder.build(CUSTOMER_ID, PAGE_ID, text); // sender = customer
    }
    private static WebHookEventDTO mockEventFromEmployee(String text) {
        return TestEventBuilder.build(PAGE_ID, CUSTOMER_ID, text); // sender = page/employee
    }

    /* Ticket factory */
    private static TicketDetailDTO ticketWithNullAssignee() {
        TicketDetailDTO t = new TicketDetailDTO();
        ProgressStatusDTO p = new ProgressStatusDTO();
        p.setId(1);
        t.setId(1);
        t.setAssignee(null);
        t.setProgressStatus(p);
        return t;
    }
    private static TicketDetailDTO ticketWithAssignee() {
        TicketDetailDTO t = ticketWithNullAssignee();
        EmployeeDTO dto = new EmployeeDTO();
        ProgressStatusDTO p = new ProgressStatusDTO();
        p.setId(1);
        dto.setUsername("john");
        t.setAssignee(dto); // giả lập DTO con
        t.setProgressStatus(p);
        return t;
    }
    /* FB user factory */
    private static FacebookUserDetailDTO facebookUser() {
        FacebookUserDetailDTO u = new FacebookUserDetailDTO();
        u.setFacebookId(CUSTOMER_ID);
        u.setFacebookName("Teddie");
        return u;
    }

    /* Shorthand cho APIResultSet */
    private static final class Api {
        static <T> APIResultSet<T> ok(T data) { return APIResultSet.ok("OK", data); }
        static <T> APIResultSet<T> notFound() { return APIResultSet.notFound(); }
    }

}
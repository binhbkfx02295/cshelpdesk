package com.binhbkfx02295.cshelpdesk.employee_management.authentication.service;

import com.binhbkfx02295.cshelpdesk.authentication.dto.LoginRequestDTO;
import com.binhbkfx02295.cshelpdesk.authentication.dto.LoginResponseDTO;
import com.binhbkfx02295.cshelpdesk.authentication.service.AuthenticationServiceImpl;
import com.binhbkfx02295.cshelpdesk.authentication.util.ValidationHelper;
import com.binhbkfx02295.cshelpdesk.authentication.util.ValidationResult;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.EmployeeDashboardDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Status;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeMapper;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.StatusLogRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.websocket.event.EmployeeEvent;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final Locale LOCALE = Locale.forLanguageTag("vi");
    private static AutoCloseable mocks;

    @Mock
    EmployeeRepository employeeRepo;

    @Mock
    StatusLogRepository statusLogRepo;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ValidationHelper validationHelper;

    @Mock
    EmployeeMapper employeeMapper;

    @Mock
    MessageSource messageSource;

    @Mock
    ApplicationEventPublisher publisher;

    @Mock
    EntityManager entityManager;


    @Mock
    MasterDataCache cache;

    @InjectMocks
    AuthenticationServiceImpl authService;

    @BeforeAll
    static void beforeAll() {
        mocks = MockitoAnnotations.openMocks(AuthenticationServiceTest.class); }

    @AfterAll
    static void afterAll()  throws Exception {
        mocks.close();
    }

    private LoginRequestDTO req(String u, String p) {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername(u);
        dto.setPassword(p);
        return dto;
    }

    private Employee employee(String username, String pwd, boolean active, int failCnt) {
        Employee e = new Employee();
        e.setUsername(username);
        e.setPassword(pwd);
        e.setActive(active);
        e.setFailedLoginCount(failCnt);
        e.setStatusLogs(new java.util.ArrayList<StatusLog>());
        return e;
    }

    private void mockValidationOk() {
        when(validationHelper.validateLoginInput(any(), any(), any()))
                .thenReturn(new ValidationResult());
    }

    @Test
    void test_login_validation_username_null_failed() {
        ValidationResult vr = new ValidationResult();
        vr.addError("username", "Sai tên đăng nhập hoặc mật khẩu");
        when(validationHelper.validateLoginInput(eq(null), any(), eq(LOCALE))).thenReturn(vr);

        APIResultSet<LoginResponseDTO> rs = authService.login(req(null, "Abcd@1234"));

        assertEquals(400, rs.getHttpCode());
        assertEquals(rs.getData().getValidationResult().getFieldErrors().get("username"), "Sai tên đăng nhập hoặc mật khẩu");
        assertNotNull(rs.getData());
        assertTrue(rs.getData().getValidationResult().hasError("username"));
    }

    @Test
    void test_login_validation_username_failed() {
        ValidationResult vr = new ValidationResult();
        vr.addError("username", "Tên đăng nhập phải có ít nhất 4 ký tự, chỉ chứa chữ và số.");
        when(validationHelper.validateLoginInput(eq("bin"), any(), eq(LOCALE))).thenReturn(vr);

        APIResultSet<LoginResponseDTO> rs = authService.login(req("bin", "Abcd@1234"));

        assertEquals(400, rs.getHttpCode());
        assertEquals(rs.getData().getValidationResult().getFieldErrors().get("username"), "Tên đăng nhập phải có ít nhất 4 ký tự, chỉ chứa chữ và số.");
        assertNotNull(rs.getData());
        assertTrue(rs.getData().getValidationResult().hasError("username"));
    }

    @Test
    void test_login_validation_password_failed() {
        ValidationResult vr = new ValidationResult();
        vr.addError("password", "Mật khẩu phải có ít nhất 8 ký tự, chứa chữ hoa, chữ thường và ký tự đặc biệt (!@#$...).");
        when(validationHelper.validateLoginInput(eq("binhbk"), eq("abcd1234"), eq(LOCALE))).thenReturn(vr);

        APIResultSet<LoginResponseDTO> rs = authService.login(req("binhbk", "abcd1234"));

        assertEquals(400, rs.getHttpCode());
        assertEquals(rs.getData().getValidationResult().getFieldErrors().get("password"), "Mật khẩu phải có ít nhất 8 ký tự, chứa chữ hoa, chữ thường và ký tự đặc biệt (!@#$...).");
    }

//    @Test
//    void test_login_employee_not_exists_failed() {
//        mockValidationOk();
//        when(employeeRepo.findWithUserGroupAndPermissionsByUsername("ghost")).thenReturn(Optional.empty());
//
//        APIResultSet<LoginResponseDTO> rs = authService.login(req("ghost", "Valid@123"));
//
//        assertEquals(400, rs.getHttpCode()); // badRequest
//        verify(employeeRepo, never()).save(any());
//    }

    @Test
    void test_login_wrong_password_failed() {
        mockValidationOk();
        Employee emp = employee("binhbk", "Abcd@1234", true, 0);
        when(employeeRepo.findWithUserGroupAndPermissionsByUsername("binhbk")).thenReturn(Optional.of(emp));
        when(passwordEncoder.matches("Abcd@123", "Abcd@1234")).thenReturn(false);
        when(messageSource.getMessage("auth.invalid.credentials.remaining", new Object[]{5 - 1}, LOCALE))
                .thenReturn("Sai tên đăng nhập hoặc mật khẩu. Bạn còn 4 lần thử.");
        APIResultSet<LoginResponseDTO> rs = authService.login(req("binhbk", "Abcd@123"));
        assertEquals(401, rs.getHttpCode()); // unauthorized
        assertEquals(1, emp.getFailedLoginCount());
        assertEquals("Sai tên đăng nhập hoặc mật khẩu. Bạn còn 4 lần thử.", rs.getMessage());
        verify(employeeRepo).save(emp);
    }

    @Test
    void test_login_employee_is_locked_failed() {
        mockValidationOk();
        Employee locked = employee("binhbk", "Abcd@1234", false, 5);
        when(employeeRepo.findWithUserGroupAndPermissionsByUsername("binhbk")).thenReturn(Optional.of(locked));
        when(messageSource.getMessage("auth.account.locked", null, LOCALE))
                .thenReturn("Tài khoản đã bị khóa");
        APIResultSet<LoginResponseDTO> rs = authService.login(req("binhbk", "Abcd@1234"));

        assertEquals(403, rs.getHttpCode()); // forbidden
        assertEquals("Tài khoản đã bị khóa", rs.getMessage()); // forbidden

    }



//    @Test
//    void test_login_wrong_password_5times_failed() {
//        mockValidationOk();
//        Employee emp = employee("john", "ENC", true, 4); // đã sai 4 lần
//        when(employeeRepo.findWithUserGroupAndPermissionsByUsername("john")).thenReturn(Optional.of(emp));
//        when(passwordEncoder.matches("Wrong@123", "ENC")).thenReturn(false);
//
//        APIResultSet<LoginResponseDTO> rs = authService.login(req("john", "Wrong@123"));
//
//        assertEquals(401, rs.getHttpCode());
//        assertFalse(emp.isActive(), "Account must be locked after 5th wrong attempt");
//        assertEquals(5, emp.getFailedLoginCount());
//        verify(employeeRepo).save(emp);
//    }

    @Test
    void test_login_success() {
        mockValidationOk();
        Employee emp = employee("binhbk", "Abcd@1234", true, 0);
        when(employeeRepo.findWithUserGroupAndPermissionsByUsername("binhbk")).thenReturn(Optional.of(emp));
        when(passwordEncoder.matches("Abcd@1234", "Abcd@1234")).thenReturn(true);
        when(statusLogRepo.findFirstByEmployee_UsernameOrderByTimestampDesc(anyString())).thenReturn(Optional.ofNullable(null));
        when(employeeRepo.save(any(Employee.class))).thenReturn(emp);
        when(cache.getEmployee(any())).thenReturn(emp);
        when(employeeMapper.toDTO(emp)).thenReturn(new EmployeeDTO());
        when(employeeMapper.toDashboardDTO(emp)).thenReturn(new EmployeeDashboardDTO());
        when(messageSource.getMessage("auth.login.success", null, LOCALE))
                .thenReturn("Đăng nhập thành công");
        APIResultSet<LoginResponseDTO> rs = authService.login(req("binhbk", "Abcd@1234"));

        assertEquals(200, rs.getHttpCode());
        assertEquals("Đăng nhập thành công", rs.getMessage());
        assertNotNull(rs.getData().getEmployeeDTO());
        assertEquals(0, emp.getFailedLoginCount());
        verify(employeeRepo).save(emp);
        verify(statusLogRepo).findFirstByEmployee_UsernameOrderByTimestampDesc("binhbk");
    }

    // @Test
    // void test_logout_success() {
    //     when(messageSource.getMessage(anyString(), any(), any())).thenReturn("msg");

    //     EmployeeDTO dto = new EmployeeDTO();
    //     dto.setUsername("john");

    //     Employee emp = employee("john", "ENC", true, 0);
    //     Status online = new Status(); online.setId(1);
    //     StatusLog lastLog = new StatusLog(); lastLog.setStatus(online);
    //     emp.setStatusLogs(new ArrayList<>(List.of(lastLog)));

    //     when(employeeRepo.findWithAllStatusLog("john")).thenReturn(Optional.of(emp));

    //     Status offline = new Status(); offline.setId(3);
    //     when(cache.getStatus(3)).thenReturn(offline);

    //     when(cache.getEmployee("john")).thenReturn(emp);
    //     when(employeeMapper.toDashboardDTO(emp)).thenReturn(new EmployeeDashboardDTO());

    //     APIResultSet<Void> rs = authService.logout(dto);

    //     assertEquals(200, rs.getHttpCode());
    //     assertEquals(2, emp.getStatusLogs().size(), "Phải thêm 1 StatusLog mới");
    //     assertEquals(3, emp.getStatusLogs().get(1).getStatus().getId(), "Status mới phải là OFFLINE");

    //     verify(employeeRepo).saveAndFlush(emp);               // lưu lại Employee
    //     verify(publisher).publishEvent(any(EmployeeEvent.class));                // gửi event realtime
    //     verify(cache).updateAllEmployees();                   // refresh cache
    // }


}
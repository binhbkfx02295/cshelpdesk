package com.binhbkfx02295.cshelpdesk.employee_management.employee.controller;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.*;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.service.EmployeeServiceImpl;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.security.auth.UserPrincipal;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResponseEntityHelper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-management")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;
    private final MasterDataCache cache;


    @PostMapping
    public ResponseEntity<EmployeeDTO> createUser(@RequestBody @Valid EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createUser(employeeDTO));
    }

    @GetMapping
    public ResponseEntity<EmployeeDTO> getUserByUsername(@RequestParam(value = "username", required = false) String username) {
        return ResponseEntity.ok(employeeService.getUserByUsername(username));
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<List<EmployeeDTO>> getAllUsers() {
        return ResponseEntity.ok(employeeService.getAllUsers());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<EmployeeDashboardDTO>> dashboard() {
        return ResponseEntity.ok(employeeService.getForDashboard());
    }

    //Employee profile
    @GetMapping("/me")
    public ResponseEntity<UserPrincipal> getUserProfile(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<EmployeeDetailDTO> updateProfile(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateUser(user.getUsername(), employeeDTO));
    }

    @PutMapping
    public ResponseEntity<EmployeeDetailDTO> updateUser(
            @RequestBody @Valid EmployeeDTO dto
    ) {
        log.info("update user: {}", dto);
        return ResponseEntity.ok(employeeService.updateUser(dto.getUsername(), dto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody ChangePasswordDTO changePasswordDTO) {

        employeeService.changePassword(
                user.getUsername(),
                changePasswordDTO.getPassword(),
                changePasswordDTO.getNewPassword());

        return ResponseEntity.ok(null);

    }

    @GetMapping("/me/online-status")
    public ResponseEntity<StatusLogDTO> getOnlineStatus(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(employeeService.getLatestOnlineStatus(user.getUsername()));
    }

    @GetMapping("/me/update-cache")
    public ResponseEntity<StatusLogDTO> updatecache(
            @AuthenticationPrincipal UserPrincipal user) {
        cache.updateAllEmployees();
        return ResponseEntity.ok(employeeService.getLatestOnlineStatus(user.getUsername()));
    }

    @PutMapping("/me/online-status")
    public ResponseEntity<Void> updateOnlineStatus(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody StatusLogDTO logDTO ) {
        employeeService.updateOnlineStatus(logDTO);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid ResetPasswordDTO resetPasswordDTO
    ) {
        employeeService.resetPassword(resetPasswordDTO);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(
            @RequestBody EmployeeDTO employeeDTO
    ) {
        employeeService.deleteByUsername(employeeDTO.getUsername());
        return ResponseEntity.ok(null);
    }

}

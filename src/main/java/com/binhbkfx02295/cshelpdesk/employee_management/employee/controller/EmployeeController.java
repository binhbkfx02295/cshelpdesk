package com.binhbkfx02295.cshelpdesk.employee_management.employee.controller;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.*;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.service.EmployeeServiceImpl;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.security.auth.UserPrincipal;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResponseEntityHelper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasRole('SUPERVISOR')")
    @PostMapping
    public ResponseEntity<APIResultSet<EmployeeDTO>> createUser(@RequestBody EmployeeDTO employeeDTO) {

        return APIResponseEntityHelper.from(employeeService.createUser(employeeDTO));
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @GetMapping("")
    public ResponseEntity<APIResultSet<EmployeeDTO>> getUserByUsername(@RequestParam(value = "username", required = false) String username) {
        return APIResponseEntityHelper.from(employeeService.getUserByUsername(username));
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @GetMapping("/get-all-user")
    public ResponseEntity<APIResultSet<List<EmployeeDTO>>> getAllUsers() {
        return APIResponseEntityHelper.from(employeeService.getAllUsers());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<APIResultSet<List<EmployeeDashboardDTO>>> dashboard() {
        return APIResponseEntityHelper.from(employeeService.getForDashboard());
    }

    //Employee profile
    @GetMapping("/me")
    public ResponseEntity<APIResultSet<UserPrincipal>> getUserProfile(
            @AuthenticationPrincipal UserPrincipal user) {
        return APIResponseEntityHelper.from(APIResultSet.ok("OK", user));
    }

    @PutMapping("/me")
    public ResponseEntity<APIResultSet<EmployeeDetailDTO>> updateProfile(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody EmployeeDTO employeeDTO) {
        return APIResponseEntityHelper.from(employeeService.updateUser(user.getUsername(), employeeDTO));
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @PutMapping
    public ResponseEntity<APIResultSet<EmployeeDetailDTO>> updateUser(
            @RequestBody EmployeeDTO dto
    ) {
        log.info(dto.toString());
        return APIResponseEntityHelper.from(employeeService.updateUser(dto.getUsername(), dto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<APIResultSet<Void>> changePassword(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        return APIResponseEntityHelper.from(employeeService.changePassword(
                user.getUsername(),
                changePasswordDTO.getPassword(),
                changePasswordDTO.getNewPassword()));
    }

    @GetMapping("/me/online-status")
    public ResponseEntity<APIResultSet<StatusLogDTO>> getOnlineStatus(
            @AuthenticationPrincipal UserPrincipal user) {
        return APIResponseEntityHelper.from(employeeService.getLatestOnlineStatus(user.getUsername()));
    }

    @GetMapping("/me/update-cache")
    public ResponseEntity<APIResultSet<StatusLogDTO>> updatecache(
            @AuthenticationPrincipal UserPrincipal user) {
        cache.updateAllEmployees();
        return APIResponseEntityHelper.from(employeeService.getLatestOnlineStatus(user.getUsername()));
    }

    @PutMapping("/me/online-status")
    public ResponseEntity<APIResultSet<Void>> updateOnlineStatus(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody StatusLogDTO logDTO ) {
        logDTO.setUsername(user.getUsername());
        return APIResponseEntityHelper.from(employeeService.updateOnlineStatus(logDTO));
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @PutMapping("/reset-password")
    public ResponseEntity<APIResultSet<Void>> resetPassword(
            @RequestBody ResetPasswordDTO resetPasswordDTO
    ) {
        return APIResponseEntityHelper.from(employeeService.resetPassword(resetPasswordDTO));
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @DeleteMapping()
    public ResponseEntity<APIResultSet<Void>> deleteUser(
            @RequestBody EmployeeDTO employeeDTO
    ) {
        return APIResponseEntityHelper.from(employeeService.deleteByUsername(employeeDTO.getUsername()));
    }

}

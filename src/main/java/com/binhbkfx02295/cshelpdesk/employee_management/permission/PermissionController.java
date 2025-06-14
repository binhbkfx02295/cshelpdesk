package com.binhbkfx02295.cshelpdesk.employee_management.permission;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-management/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public APIResultSet<List<PermissionDTO>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public APIResultSet<PermissionDTO> getPermissionById(@PathVariable String id) {
        return permissionService.getPermissionById(id);
    }

    @PostMapping
    public APIResultSet<PermissionDTO> createPermission(@RequestBody PermissionDTO dto) {
        return permissionService.createPermission(dto);
    }

    @PutMapping("/{id}")
    public APIResultSet<PermissionDTO> updatePermission(@PathVariable String id, @RequestBody PermissionDTO dto) {
        return permissionService.updatePermission(id, dto);
    }

    @DeleteMapping("/{id}")
    public APIResultSet<Boolean> deletePermission(@PathVariable String id) {
        return permissionService.deletePermission(id);
    }
}

package com.binhbkfx02295.cshelpdesk.employee_management.permission;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-management/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    // Lấy danh sách permission
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    // Lấy permission theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable int id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    // Tạo mới permission
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.createPermission(dto));
    }

    // Cập nhật permission
    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable int id, @RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, dto));
    }

    // Xoá permission
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable String id) {
        return ResponseEntity.ok(null);
    }
}

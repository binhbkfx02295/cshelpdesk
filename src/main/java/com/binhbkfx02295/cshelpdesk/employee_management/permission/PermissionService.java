package com.binhbkfx02295.cshelpdesk.employee_management.permission;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);
    PermissionDTO updatePermission(int id, PermissionDTO permissionDTO);
    void deletePermission(int id);
    PermissionDTO getPermissionById(int id);
    List<PermissionDTO> getAllPermissions();
}

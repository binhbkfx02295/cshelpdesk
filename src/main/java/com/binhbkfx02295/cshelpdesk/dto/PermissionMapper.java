package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    public PermissionDTO toDTO(Permission entity) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;

    }

    public Permission toEntity(PermissionDTO dto) {
        Permission entity = new Permission();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setId(dto.getId());
        return entity;

    }
}

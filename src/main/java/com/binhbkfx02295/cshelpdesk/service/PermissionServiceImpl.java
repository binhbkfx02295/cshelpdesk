package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.PermissionDTO;
import com.binhbkfx02295.cshelpdesk.repository.PermissionRepository;
import com.binhbkfx02295.cshelpdesk.entity.Permission;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDTO createPermission(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return convertToDTO(permissionRepository.save(permission));
    }

    @Override
    public PermissionDTO updatePermission(int id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("he"));

        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return convertToDTO(permissionRepository.save(permission));
    }

    @Override
    public void deletePermission(int id) {
        if (!permissionRepository.existsById(id)) {
            throw new EntityNotFoundException("Khong tim thay permission id");
        }

        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionDTO getPermissionById(int id) {
        return convertToDTO(permissionRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Khong tim thay permission " + id)));
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        return dto;
    }
}

package com.binhbkfx02295.cshelpdesk.employee_management.usergroup;

import com.binhbkfx02295.cshelpdesk.config.ForeignKeyConstraintException;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;
import com.binhbkfx02295.cshelpdesk.employee_management.permission.PermissionMapper;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.repository.EmployeeRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final EmployeeRepository employeeRepository;
    private final UserGroupMapper mapper;
    private final PermissionMapper permissionMapper;
    private final MasterDataCache cache;

    @Override
    public UserGroupDTO createGroup(UserGroupDTO groupDTO) {
        UserGroup group = mapper.toEntity(groupDTO);
        group.setPermissions(groupDTO.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        group.setDescription(groupDTO.getDescription());
        return mapper.toDTO(userGroupRepository.save(group));
    }

    @Override
    public UserGroupDTO updateGroup(int id, UserGroupDTO groupDTO) {
        UserGroup group = userGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhóm có ID: " + id));

        group.setName(groupDTO.getName());
        group.setCode(groupDTO.getCode());
        group.setPermissions(groupDTO.getPermissions().stream().map(permissionMapper::toEntity).collect(Collectors.toSet()));
        group.setDescription(groupDTO.getDescription());
        return mapper.toDTO(userGroupRepository.save(group));
    }

    @Override
    public void deleteGroup(int id) {
        if (userGroupRepository.existsById(id))
            throw new EntityNotFoundException("Không tìm thấy nhóm quyền ID để xoá: " + id);

        if (!employeeRepository.existsByUserGroup_GroupId(id))
            throw new ForeignKeyConstraintException("Không thể xoá nhóm quyền vì vẫn còn nhân viên tham chiếu.");

        userGroupRepository.deleteById(id);
    }

    @Override
    public UserGroupDTO getGroupById(int id) {
        return mapper.toDTO(userGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nhóm quyền ID không tồn tại " + id)));
    }

    @Override
    public List<UserGroupDTO> getAllGroups() {
        return userGroupRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

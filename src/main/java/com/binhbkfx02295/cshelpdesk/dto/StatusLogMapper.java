package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Employee;
import com.binhbkfx02295.cshelpdesk.entity.StatusLog;
import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class StatusLogMapper {

    private final StatusMapper statusMapper;
    private final MasterDataCache cache;

    public StatusLogDTO toDTO(StatusLog entity) {
        StatusLogDTO statusLogDTO = new StatusLogDTO();
        statusLogDTO.setStatus(statusMapper.toDTO(entity.getStatus()));
        statusLogDTO.setFrom(entity.getTimestamp());
        statusLogDTO.setUsername(entity.getEmployee().getUsername());
        return statusLogDTO;
    }

    public StatusLog toEntity(StatusLogDTO dto) {
        StatusLog entity = new StatusLog();
        entity.setStatus(statusMapper.toEntity(dto.getStatus()));
        entity.setTimestamp(dto.getFrom());
        if (entity.getTimestamp() == null) {
            entity.setTimestamp(new Timestamp(System.currentTimeMillis()));
        }
        if (dto.getUsername() != null) {
            Employee employee = cache.getEmployee(dto.getUsername());
            entity.setEmployee(employee);
        }
        return entity;
    }
}

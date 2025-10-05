package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.StatusLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class StatusLogMapper {

    private final StatusMapper statusMapper;

    public StatusLogDTO toDTO(StatusLog entity) {
        StatusLogDTO statusLogDTO = new StatusLogDTO();
        statusLogDTO.setStatus(statusMapper.toDTO(entity.getStatus()));
        statusLogDTO.setFrom(entity.getTimestamp());
        statusLogDTO.setUsername(entity.getEmployee().getUsername());
        return statusLogDTO;
    }

    public StatusLog toEntity(StatusLogDTO dto) {
        //TODO:
        throw new UnsupportedOperationException("TODO");
    }
}

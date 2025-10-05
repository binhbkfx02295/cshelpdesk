package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public Status toEntity(StatusDTO dto) {
        Status entity = new Status();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    public StatusDTO toDTO(Status entity) {
        StatusDTO dto = new StatusDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}

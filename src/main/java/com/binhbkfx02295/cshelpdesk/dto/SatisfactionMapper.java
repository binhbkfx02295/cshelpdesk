package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Satisfaction;
import org.springframework.stereotype.Component;

@Component
public class SatisfactionMapper {

    public SatisfactionDTO toDTO(Satisfaction satisfaction) {
        if (satisfaction == null) return null;
        SatisfactionDTO dto = new SatisfactionDTO();
        dto.setId(satisfaction.getId());
        dto.setCode(satisfaction.getCode());
        dto.setName(satisfaction.getName());
        return dto;
    };

    public Satisfaction toEntity(SatisfactionDTO dto) {
        Satisfaction entity = new Satisfaction();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        return entity;
    }
}

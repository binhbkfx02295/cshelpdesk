package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        if (category == null) return dto;
        dto.setName(category.getName());
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        return dto;
    };

    public Category toEntity(CategoryDTO dto) {
        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        return entity;
    }
}

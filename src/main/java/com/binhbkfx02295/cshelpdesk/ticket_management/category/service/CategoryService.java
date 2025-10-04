package com.binhbkfx02295.cshelpdesk.ticket_management.category.service;

import com.binhbkfx02295.cshelpdesk.ticket_management.category.dto.CategoryDTO;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface CategoryService {
    CategoryDTO create(CategoryDTO category);
    CategoryDTO update(int id, CategoryDTO category);
    void delete(int id);
    List<CategoryDTO> getAll();
}

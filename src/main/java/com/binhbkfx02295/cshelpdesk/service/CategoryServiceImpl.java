package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.entity.Category;
import com.binhbkfx02295.cshelpdesk.dto.CategoryDTO;
import com.binhbkfx02295.cshelpdesk.dto.CategoryMapper;
import com.binhbkfx02295.cshelpdesk.repository.CategoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MasterDataCache cache;
    private final CategoryMapper mapper;

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByCode(categoryDTO.getCode())) {
            throw new EntityExistsException("Mã danh mục đã tồn tại.");
        }
        return mapper.toDTO(categoryRepository.save(mapper.toEntity(categoryDTO)));
    }


    @Override
    public CategoryDTO update(int id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục cần cập nhật."));
        category.setName(categoryDTO.getName());
        category.setCode(categoryDTO.getCode());
        return mapper.toDTO(categoryRepository.save(mapper.toEntity(categoryDTO)));
    }

    @Override
    public void delete(int id) {
        categoryRepository.deleteById(id);

    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

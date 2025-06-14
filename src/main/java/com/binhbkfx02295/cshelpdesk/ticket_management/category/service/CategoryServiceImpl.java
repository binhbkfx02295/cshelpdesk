package com.binhbkfx02295.cshelpdesk.ticket_management.category.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.entity.Category;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.dto.CategoryDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.mapper.CategoryMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.category.repository.CategoryRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MasterDataCache cache;
    private final CategoryMapper mapper;

    @Override
    public APIResultSet<CategoryDTO> create(CategoryDTO categoryDTO) {
        try {
            if (categoryRepository.existsByCode(categoryDTO.getCode())) {
                return APIResultSet.badRequest("Mã danh mục đã tồn tại.");
            }
            Category saved = categoryRepository.save(mapper.toEntity(categoryDTO));
            cache.getAllCategories().put(saved.getId(), saved);
            return APIResultSet.ok("Tạo danh mục thành công.", mapper.toDTO(saved));
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError("Đã xảy ra lỗi khi tạo danh mục.");
        }
    }



    @Override
    public APIResultSet<CategoryDTO> update(int id, CategoryDTO categoryDTO) {
        try {
            Optional<Category> optional = categoryRepository.findById(id);
            if (optional.isEmpty()) {
                return APIResultSet.notFound("Không tìm thấy danh mục cần cập nhật.");
            }
            Category existing = optional.get();
            existing.setName(categoryDTO.getName());
            existing.setCode(categoryDTO.getCode());
            Category updated = categoryRepository.save(mapper.toEntity(categoryDTO));
            if (cache.getAllCategories().containsKey(updated.getId())) {
                cache.getAllCategories().put(updated.getId(), updated);
            }
            return APIResultSet.ok("Cập nhật danh mục thành công.", mapper.toDTO(updated));
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError("Đã xảy ra lỗi khi cập nhật danh mục.");
        }
    }

    @Override
    public APIResultSet<Void> delete(int id) {
        try {
            categoryRepository.deleteById(id);
            cache.getAllCategories().remove(id);
            return APIResultSet.ok("Xóa danh mục thành công.", null);
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError("Không thể xóa danh mục. Có thể đang được liên kết với dữ liệu khác.");
        }
    }

    @Override
    public APIResultSet<List<CategoryDTO>> getAll() {
        try {
            List<Category> categories = cache.getAllCategories().values().stream().toList();
            return APIResultSet.ok("Lấy danh sách danh mục thành công.", categories.stream().map(mapper::toDTO).toList());
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError("Đã xảy ra lỗi khi lấy danh sách danh mục.");
        }
    }
}

package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.dto.TagDTO;
import com.binhbkfx02295.cshelpdesk.entity.Tag;
import com.binhbkfx02295.cshelpdesk.dto.TagMapper;
import com.binhbkfx02295.cshelpdesk.repository.TagRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final MasterDataCache cache;
    private TagMapper mapper;

    @Override
    public TagDTO create(TagDTO tagDTO) {
        if (tagRepository.existsByName(tagDTO.getName())) {
            throw new EntityExistsException("Hashtag đã tồn tại: " + tagDTO.getName());
        }

        return mapper.toDTO(tagRepository.save(Tag.builder()
                .name(tagDTO.getName())
                .build()));
    }

    @Override
    public TagDTO update(int id, TagDTO newTagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tag ID: " + id));
        tag.setName(newTagDTO.getName());
        return mapper.toDTO(tagRepository.save(tag));
    }

    @Override
    public void delete(int id) {
        if (!tagRepository.existsById(id))
            throw new EntityNotFoundException("Không tìm thấy hashtag để xóa ID: " + id);
        tagRepository.deleteById(id);
    }

    @Override
    public List<TagDTO> search(String keyword) {
        return tagRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<TagDTO> getAll() {
        return tagRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

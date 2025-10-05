package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO create(TagDTO tag);
    TagDTO update(int id, TagDTO tag);
    void delete(int id);
    List<TagDTO> search(String keyword);
    List<TagDTO> getAll();
}

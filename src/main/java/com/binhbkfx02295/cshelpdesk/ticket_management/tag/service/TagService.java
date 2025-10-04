package com.binhbkfx02295.cshelpdesk.ticket_management.tag.service;

import com.binhbkfx02295.cshelpdesk.ticket_management.tag.dto.TagDTO;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface TagService {
    TagDTO create(TagDTO tag);
    TagDTO update(int id, TagDTO tag);
    void delete(int id);
    List<TagDTO> search(String keyword);
    List<TagDTO> getAll();
}

package com.binhbkfx02295.cshelpdesk.dto;

import com.binhbkfx02295.cshelpdesk.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

     public TagDTO toDTO(Tag tag) {
          TagDTO dto = new TagDTO();
          dto.setId(tag.getId());
          dto.setName(tag.getName());
          return dto;
     };

     public Tag toEntity(TagDTO dto) {
          Tag tag = new Tag();
          tag.setName(dto.getName());
          tag.setId(dto.getId());
          return tag;
     };

}

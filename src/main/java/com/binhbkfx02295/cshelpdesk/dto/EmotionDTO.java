package com.binhbkfx02295.cshelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmotionDTO {
    private int id;
    String code;
    String name;
}

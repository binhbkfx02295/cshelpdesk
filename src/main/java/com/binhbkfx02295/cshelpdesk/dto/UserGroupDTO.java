package com.binhbkfx02295.cshelpdesk.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserGroupDTO {
    private int groupId;
    private String name;
    private String code;
    private List<PermissionDTO> permissions = new ArrayList<>();
    private String description;
}
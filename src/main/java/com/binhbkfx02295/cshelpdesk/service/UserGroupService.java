package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.UserGroupDTO;

import java.util.List;

public interface UserGroupService {
    UserGroupDTO createGroup(UserGroupDTO groupDTO);
    UserGroupDTO updateGroup(int id, UserGroupDTO groupDTO);
    void deleteGroup(int id);
    UserGroupDTO getGroupById(int id);
    List<UserGroupDTO> getAllGroups();
}

package com.binhbkfx02295.cshelpdesk.employee_management.usergroup;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface UserGroupService {
    UserGroupDTO createGroup(UserGroupDTO groupDTO);
    UserGroupDTO updateGroup(int id, UserGroupDTO groupDTO);
    void deleteGroup(int id);
    UserGroupDTO getGroupById(int id);
    List<UserGroupDTO> getAllGroups();
}

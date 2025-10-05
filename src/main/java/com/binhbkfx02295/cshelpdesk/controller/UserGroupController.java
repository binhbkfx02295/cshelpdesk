package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.dto.UserGroupDTO;
import com.binhbkfx02295.cshelpdesk.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-management/user-group")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping
    public ResponseEntity<UserGroupDTO> createGroup(@RequestBody UserGroupDTO dto) {
        return ResponseEntity.ok(userGroupService.createGroup(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroupDTO> updateGroup(@PathVariable int id,
                                                                  @RequestBody UserGroupDTO dto) {
        return ResponseEntity.ok(userGroupService.updateGroup(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable int id) {
        userGroupService.deleteGroup(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroupDTO> getGroupById(@PathVariable int id) {
        return ResponseEntity.ok(userGroupService.getGroupById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserGroupDTO>> getAllGroups() {
        return ResponseEntity.ok(userGroupService.getAllGroups());
    }
}

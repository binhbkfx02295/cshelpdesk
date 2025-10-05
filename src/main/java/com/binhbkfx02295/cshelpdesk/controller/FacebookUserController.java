package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.util.FacebookUserExporter;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserExportDTO;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserListDTO;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserDetailDTO;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserSearchCriteria;
import com.binhbkfx02295.cshelpdesk.service.FacebookUserService;
import com.binhbkfx02295.cshelpdesk.service.FacebookUserServiceImpl;
import com.binhbkfx02295.cshelpdesk.util.PaginationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/facebookuser")
@Slf4j
public class FacebookUserController {

    FacebookUserService facebookUserService;

    @Autowired
    public FacebookUserController(FacebookUserServiceImpl service) {
        facebookUserService = service;
    }

    @GetMapping("/ping")
    public String ping() {
        return "it works";
    }

    //get all
    @GetMapping(params = "!id")
    public ResponseEntity<List<FacebookUserListDTO>> getAll() {
        return ResponseEntity.ok(facebookUserService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<FacebookUserDetailDTO> get(@RequestParam String id) {
        return ResponseEntity.ok(facebookUserService.get(id));
    }

    @PostMapping
    public ResponseEntity<FacebookUserDetailDTO> createFacebookUser(@RequestBody FacebookUserDetailDTO user) {
        return ResponseEntity.ok(facebookUserService.save(user));
    }


    @PutMapping()
    public ResponseEntity<FacebookUserDetailDTO> updateFacebookUser(@RequestBody FacebookUserDetailDTO user) {
        return ResponseEntity.ok(facebookUserService.update(user));
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationResponse<FacebookUserDetailDTO>> searchFacebookUser(
            @ModelAttribute FacebookUserSearchCriteria criteria,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(facebookUserService.searchUsers(criteria, pageable));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFacebookUser(@RequestParam String id) {
        facebookUserService.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/export-excel")
    public ResponseEntity<InputStreamResource> exportExcel(
            @ModelAttribute FacebookUserSearchCriteria criteria) {
        List<FacebookUserExportDTO> result = facebookUserService.exportSearchUsers(criteria, Pageable.unpaged());
        if (result == null) {
            return ResponseEntity.internalServerError().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Khach Hang.xlsx");
        ByteArrayInputStream in = FacebookUserExporter.exportToExcel(result);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAll(
            @RequestBody List<String> ids
            ) {
        facebookUserService.deleteAll(ids);
        return ResponseEntity.ok(null);
    }



}

package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.dto.ProgressStatusDTO;
import com.binhbkfx02295.cshelpdesk.service.ProgressStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/api/progress-status")
@RequiredArgsConstructor
@Slf4j
public class ProgressStatusController {

    private final ProgressStatusServiceImpl progressStatusService;

    @GetMapping
    public ResponseEntity<List<ProgressStatusDTO>> getAllProgressStatus() {
        return ResponseEntity.ok(progressStatusService.getAllProgressStatus());
    }
}

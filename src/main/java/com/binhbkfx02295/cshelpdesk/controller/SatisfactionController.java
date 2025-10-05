package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.dto.SatisfactionDTO;
import com.binhbkfx02295.cshelpdesk.service.SatisfactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/satisfaction")
@RequiredArgsConstructor

public class SatisfactionController {

    private final SatisfactionServiceImpl satisfactionService;

    @GetMapping
    public ResponseEntity<List<SatisfactionDTO>> getAllSatisfaction() {
        return ResponseEntity.ok(satisfactionService.getAllSatisfaction());
    }
}

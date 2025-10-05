package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.dto.EmotionDTO;
import com.binhbkfx02295.cshelpdesk.service.EmotionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/emotion")
@Slf4j
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionServiceImpl emotionService;

    @GetMapping
    public ResponseEntity<List<EmotionDTO>> getAllEmotion() {
        return ResponseEntity.ok(emotionService.getAllEmotion());
    }
}

package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.service.TagService;
import com.binhbkfx02295.cshelpdesk.dto.TagDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/search")
    public ResponseEntity<List<TagDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(tagService.search(keyword));
    }

    @GetMapping()
    public ResponseEntity<List<TagDTO>> getAll() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @PostMapping
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.create(tagDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(@PathVariable int id, @RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.update(id, tagDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        tagService.delete(id);
        return ResponseEntity.ok(null);
    }
}

package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.EmotionDTO;
import com.binhbkfx02295.cshelpdesk.dto.EmotionMapper;
import com.binhbkfx02295.cshelpdesk.repository.EmotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService{

    private final EmotionMapper mapper;
    private final EmotionRepository repository;

    @Override
    public List<EmotionDTO> getAllEmotion() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

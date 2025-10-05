package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.dto.SatisfactionDTO;
import com.binhbkfx02295.cshelpdesk.dto.SatisfactionMapper;
import com.binhbkfx02295.cshelpdesk.repository.SatisfactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SatisfactionServiceImpl implements SatisfactionService {

    private final MasterDataCache cache;
    private final SatisfactionMapper mapper;
    private final SatisfactionRepository repo;

    @Override
    public List<SatisfactionDTO> getAllSatisfaction() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}

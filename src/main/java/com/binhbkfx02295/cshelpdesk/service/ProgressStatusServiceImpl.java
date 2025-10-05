package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.ProgressStatusDTO;
import com.binhbkfx02295.cshelpdesk.dto.ProgressStatusMapper;
import com.binhbkfx02295.cshelpdesk.repository.ProgressStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProgressStatusServiceImpl implements ProgressStatusService {

    private final ProgressStatusRepository repository;
    private final ProgressStatusMapper mapper;

    @Override
    public List<ProgressStatusDTO> getAllProgressStatus() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }




}

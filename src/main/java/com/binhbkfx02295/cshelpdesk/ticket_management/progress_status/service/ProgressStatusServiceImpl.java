package com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.dto.ProgressStatusDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.mapper.ProgressStatusMapper;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.repository.ProgressStatusRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
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
    private final MasterDataCache cache;

    @Override
    public List<ProgressStatusDTO> getAllProgressStatus() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }




}

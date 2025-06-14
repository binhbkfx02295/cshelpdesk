package com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.dto.ProgressStatusDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.progress_status.mapper.ProgressStatusMapper;
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
    private final ProgressStatusMapper mapper;
    private final MasterDataCache cache;

    @Override
    public APIResultSet<List<ProgressStatusDTO>> getAllProgressStatus() {
        try {
            return APIResultSet.ok("Lay all progress status thanh cong",
                    cache.getAllProgress().values().stream().map(mapper::toDTO).toList());
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError();
        }
    }




}

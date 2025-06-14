package com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.service;

import com.binhbkfx02295.cshelpdesk.infrastructure.common.cache.MasterDataCache;
import com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.dto.SatisfactionDTO;
import com.binhbkfx02295.cshelpdesk.ticket_management.satisfaction.mapper.SatisfactionMapper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
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

    @Override
    public APIResultSet<List<SatisfactionDTO>> getAllSatisfaction() {
        try {
            return APIResultSet.ok("Lay tat ca Muc hai long thanh cong",
                    cache.getAllSatisfactions().values().stream().map(mapper::toDTO).toList());

        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError();
        }
    }
}

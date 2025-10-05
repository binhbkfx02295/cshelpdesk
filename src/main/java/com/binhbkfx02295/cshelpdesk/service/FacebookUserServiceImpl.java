package com.binhbkfx02295.cshelpdesk.service;

import com.binhbkfx02295.cshelpdesk.dto.*;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.entity.FacebookUser;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserMapper;
import com.binhbkfx02295.cshelpdesk.repository.FacebookUserRepository;
import com.binhbkfx02295.cshelpdesk.util.PaginationResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacebookUserServiceImpl implements FacebookUserService {
    private final FacebookUserRepository facebookUserRepository;
    private final FacebookUserMapper mapper;

    @Override
    public FacebookUserDetailDTO save(FacebookUserDetailDTO userDTO) {
        return mapper.toDetailDTO(facebookUserRepository.save(mapper.toEntity(userDTO)));
    }

    @Override
    public FacebookUserDetailDTO save(FacebookUserFetchDTO userDTO) {
        return mapper.toDetailDTO(facebookUserRepository.save(mapper.toEntity(userDTO)));
    }

    @Override
    public FacebookUserDetailDTO save(FacebookUserProfileDTO userDTO) {
        return mapper.toDetailDTO(facebookUserRepository.save(mapper.toEntity(userDTO)));
    }

    @Override
    public FacebookUserDetailDTO update(FacebookUserDetailDTO updatedUserDTO) {
        FacebookUser entity = facebookUserRepository.get(updatedUserDTO.getFacebookId());
        if (entity == null)
            throw  new EntityNotFoundException(MSG_ERROR_USER_NOT_FOUND);

        return mapper.toDetailDTO(entity);
    }

    @Override
    public FacebookUserDetailDTO get(String id) {
        return mapper.toDetailDTO(Optional.ofNullable(facebookUserRepository.get(id))
                .orElseThrow(()-> new EntityNotFoundException(MSG_ERROR_USER_NOT_FOUND)));
    }

    @Override
    public List<FacebookUserListDTO> getAll() {
        return facebookUserRepository.getAll()
                .stream()
                .map(mapper::toListDTO)
                .toList();
    }


    @Override
    public void deleteById(String s) {
        if (!facebookUserRepository.existsById(s))
            throw new EntityNotFoundException(MSG_ERROR_USER_NOT_FOUND);

        facebookUserRepository.deleteById(s);
    }

    @Override
    public PaginationResponse<FacebookUserDetailDTO> searchUsers(FacebookUserSearchCriteria criteria, Pageable pageable) {
        Map<String, Object> queryResult = facebookUserRepository.search(criteria, pageable);
        List<FacebookUserDetailDTO> content = ((List<FacebookUser>)queryResult.get("content")).stream().map(mapper::toDetailDTO).toList();
        PaginationResponse<FacebookUserDetailDTO> response = new PaginationResponse<>();
        response.setContent(content);
        response.setTotalElements((long)queryResult.get("totalElements"));
        response.setPage(pageable.getPageNumber());
        response.setSize(pageable.getPageSize());
        response.setTotalPages((int) Math.ceil((double) response.getTotalElements() / response.getSize()));
        return response;
    }

    @Override
    public void deleteAll(List<String> ids) {
        facebookUserRepository.deleteAll(ids);
    }


    public List<FacebookUserExportDTO> exportSearchUsers(FacebookUserSearchCriteria criteria, Pageable pageable) {
        Map<String, Object> queryResult = facebookUserRepository.search(criteria, pageable);
        return ((List<FacebookUser>)queryResult.get("content")).stream().map(mapper::toExportDTO).toList();
    }


}

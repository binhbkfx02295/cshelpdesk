package com.binhbkfx02295.cshelpdesk.facebookuser.service;

import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.*;
import com.binhbkfx02295.cshelpdesk.facebookuser.entity.FacebookUser;
import com.binhbkfx02295.cshelpdesk.facebookuser.mapper.FacebookUserMapper;
import com.binhbkfx02295.cshelpdesk.facebookuser.repository.FacebookUserRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacebookUserServiceImpl implements FacebookUserService {

    private final FacebookUserRepository facebookUserRepository;
    private final FacebookUserMapper mapper;

    @Override
    public APIResultSet<FacebookUserDetailDTO> save(FacebookUserDetailDTO userDTO) {
        APIResultSet<FacebookUserDetailDTO> result;
        try {
            FacebookUser user = facebookUserRepository.save(mapper.toEntity(userDTO));
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(Instant.now());
            }
            result = APIResultSet.ok(MSG_SUCCESS_CREATE_FACEBOOK_USER, mapper.toDetailDTO(user));
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<FacebookUserDetailDTO> save(FacebookUserProfileDTO userDTO) {
        APIResultSet<FacebookUserDetailDTO> result;
        try {
            FacebookUser user = facebookUserRepository.save(mapper.toEntity(userDTO));

            if (user.getCreatedAt() == null) {
                user.setCreatedAt(Instant.now());
            }
            result = APIResultSet.ok(MSG_SUCCESS_CREATE_FACEBOOK_USER, mapper.toDetailDTO(user));
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError(e.getMessage());
        }

        return result;
    }

    @Override
    public APIResultSet<FacebookUserDetailDTO> update(FacebookUserDetailDTO updatedUserDTO) {
        APIResultSet<FacebookUserDetailDTO> result;
        try {
            result = Optional.ofNullable(facebookUserRepository.update(mapper.toEntity(updatedUserDTO)))
                    .map(user ->
                       APIResultSet.ok("Cập nhật thông tin khách hàng thành công.", mapper.toDetailDTO(user)))
                    .orElseGet(() -> APIResultSet.notFound(MSG_ERROR_FACEBOOK_USER_NOT_EXISTS));
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<FacebookUserDetailDTO> get(String id) {
        APIResultSet<FacebookUserDetailDTO> result;
        try {
            result = Optional.ofNullable(facebookUserRepository.get(id))
                    .map(user -> APIResultSet.ok("Truy vấn khách hàng thành công", mapper.toDetailDTO(user)))
                    .orElseGet(() -> APIResultSet.notFound(MSG_ERROR_FACEBOOK_USER_NOT_EXISTS));
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<List<FacebookUserListDTO>> getAll() {
        APIResultSet<List<FacebookUserListDTO>> result;
        try {
            result = Optional.ofNullable(facebookUserRepository.getAll())
                    .map(users -> APIResultSet.ok(MSG_SUCCESS_GET_ALL_FACEBOOK_USER,
                            users.stream()
                                    .sorted((o1, o2) -> (int)o2.getCreatedAt().toEpochMilli() - (int)o1.getCreatedAt().toEpochMilli())
                                    .map(mapper::toListDTO)
                                    .toList()))
                    .orElseGet(() -> APIResultSet.notFound(MSG_ERROR_GET_ALL_FACEBOOK_USER));

        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<Void> existsById(String facebookId) {
        try {
            if (facebookUserRepository.existsById(facebookId))
                return APIResultSet.ok("Khách hàng có tồn tại", null);
            return APIResultSet.notFound(MSG_ERROR_FACEBOOK_USER_NOT_EXISTS);
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.notFound();
        }
    }

    @Override
    public APIResultSet<Void> deleteById(String s) {
        try {
            if (s == null || s.isEmpty() || s.isBlank()) {
                return APIResultSet.badRequest(MSG_ERROR_VALIDATE_FACEBOOK_USER);
            }
            if (!facebookUserRepository.existsById(s)) {
                return APIResultSet.badRequest(MSG_ERROR_FACEBOOK_USER_NOT_EXISTS);
            }
            facebookUserRepository.deleteById(s);
            return APIResultSet.ok(MSG_SUCCESS_DELETE_FACEBOOK_USER, null);
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError();
        }
    }

    @Override
    public APIResultSet<PaginationResponse<FacebookUserDetailDTO>> searchUsers(FacebookUserSearchCriteria criteria, Pageable pageable) {
        APIResultSet<PaginationResponse<FacebookUserDetailDTO>> result;
        try {
            Map<String, Object> queryResult = facebookUserRepository.search(criteria, pageable);
            List<FacebookUserDetailDTO> content = ((List<FacebookUser>)queryResult.get("content")).stream().map(mapper::toDetailDTO).toList();
            PaginationResponse<FacebookUserDetailDTO> response = new PaginationResponse<>();
            response.setContent(content);
            response.setTotalElements((long)queryResult.get("totalElements"));
            response.setPage(pageable.getPageNumber());
            response.setSize(pageable.getPageSize());
            response.setTotalPages((int) Math.ceil((double) response.getTotalElements() / response.getSize()));
            result = APIResultSet.ok(MSG_SUCCESS_GET_ALL_FACEBOOK_USERS, response);
        } catch (Exception e) {
            log.error("Error message", e);
            result = APIResultSet.internalError();
        }
        return result;
    }

    @Override
    public APIResultSet<Void> deleteAll(ArrayList<String> ids) {
        try {
            facebookUserRepository.deleteAll(ids);
            return APIResultSet.ok(MSG_ERROR_DELETE_FACEBOOK_USERS, null);
        } catch (Exception e) {
            log.error("Error message", e);
            return APIResultSet.internalError();
        }
    }



    public List<FacebookUserExportDTO> exportSearchUsers(FacebookUserSearchCriteria criteria, Pageable pageable) {
        try {
            Map<String, Object> queryResult = facebookUserRepository.search(criteria, pageable);
            if (queryResult.get("content") != null) {
                return ((List<FacebookUser>)queryResult.get("content")).stream().map(mapper::toExportDTO).toList();
            }
        } catch (Exception e) {
            log.error("Error message", e);
        }
        return null;
    }


}

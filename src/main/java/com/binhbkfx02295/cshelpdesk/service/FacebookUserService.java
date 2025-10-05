package com.binhbkfx02295.cshelpdesk.service;


import com.binhbkfx02295.cshelpdesk.dto.*;
import com.binhbkfx02295.cshelpdesk.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.util.PaginationResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacebookUserService {

    static final String MSG_ERROR_USER_NOT_FOUND = "MSG_ERROR_USER_NOT_FOUND";

    static final String MSG_ERROR_SEARCH_NOT_FOUND = "MSG_ERROR_SEARCH_NOT_FOUND";

    static final String MSG_SUCCESS = "Success";

    static final String MSG_ERROR_BAD_REQUEST = "MSG_ERROR_BAD_REQUEST";

    static final String MSG_ERROR_INTERNAL_SERVER_ERROR = "MSG_ERROR_INTERNAL_SERVER_ERROR";

    static final String MSG_ERROR_NOT_ALLOWED = "MSG_ERROR_NOT_ALLOWED";

    FacebookUserDetailDTO save(FacebookUserDetailDTO save);

    FacebookUserDetailDTO save(FacebookUserFetchDTO save);

    FacebookUserDetailDTO update(FacebookUserDetailDTO updatedUser);

    FacebookUserDetailDTO get(String id);

    List<FacebookUserListDTO> getAll();

    void deleteById(String s);

    PaginationResponse<FacebookUserDetailDTO> searchUsers(FacebookUserSearchCriteria criteria, Pageable pageable);

    void deleteAll(List<String> ids);

    FacebookUserDetailDTO save(FacebookUserProfileDTO profile);

    List<FacebookUserExportDTO> exportSearchUsers(FacebookUserSearchCriteria criteria, Pageable unpaged);
}

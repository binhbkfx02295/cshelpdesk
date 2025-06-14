package com.binhbkfx02295.cshelpdesk.facebookuser.service;


import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserListDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserDetailDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserSearchCriteria;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.PaginationResponse;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface FacebookUserService {
    String MSG_SUCCESS_CREATE_FACEBOOK_USER = "Khởi tạo người dùng Facebook thành công";
    String MSG_SUCCESS_GET_ALL_FACEBOOK_USER = "Truy vấn tất cả khách hàng thành công";
    String MSG_ERROR_GET_ALL_FACEBOOK_USER = "Truy vấn tất cả khách hàng thất bại";
    String MSG_ERROR_VALIDATE_FACEBOOK_USER = "Lỗi ID khách hàng bị thiếu";
    String MSG_ERROR_FACEBOOK_USER_NOT_EXISTS = "Khách hàng không tồn tại";
    String MSG_SUCCESS_DELETE_FACEBOOK_USER = "Xóa Khách hàng thành công.";
    String MSG_SUCCESS_GET_ALL_FACEBOOK_USERS = "Truy vấn tất cả khách hàng thành công.";
    String MSG_ERROR_DELETE_FACEBOOK_USERS = "Xóa nhóm khách hàng thành công.";


    APIResultSet<FacebookUserDetailDTO> save(FacebookUserDetailDTO save);

    APIResultSet<FacebookUserDetailDTO> update(FacebookUserDetailDTO updatedUser);

    APIResultSet<FacebookUserDetailDTO> get(String id);

    APIResultSet<List<FacebookUserListDTO>> getAll();

    APIResultSet<Void> existsById(String facebookId);

    APIResultSet<Void> deleteById(String s);

    APIResultSet<PaginationResponse<FacebookUserDetailDTO>> searchUsers(FacebookUserSearchCriteria criteria, Pageable pageable);

    APIResultSet<Void> deleteAll(ArrayList<String> ids);

    APIResultSet<FacebookUserDetailDTO> save(FacebookUserProfileDTO profile);
}

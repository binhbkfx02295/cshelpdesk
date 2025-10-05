package com.binhbkfx02295.cshelpdesk.service;
import com.binhbkfx02295.cshelpdesk.dto.LoginResponse;
import com.binhbkfx02295.cshelpdesk.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.dto.LoginRequest;


import javax.security.auth.login.AccountLockedException;

public interface AuthenticationService {

    /**
     * Đăng nhập với username và password
     * @param request chứa username và password
     * @return APIResultSet<LoginResponseDTO>: Nếu lỗi validate -> BAD_REQUEST với ValidationResult,
     * Nếu sai thông tin đăng nhập -> OK với message lỗi và data null.
     * Nếu thành công -> OK với message thành công và LoginResponseDTO chứa thông tin user
     */
    LoginResponse login(LoginRequest request) throws AccountLockedException;

    /**
     * Đăng xuất và huỷ session hiện tại
     */
    void logout(EmployeeDTO employeeDTO);
}

package com.binhbkfx02295.cshelpdesk.employee_management.employee.service;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.dto.*;
import com.binhbkfx02295.cshelpdesk.employee_management.employee.mapper.EmployeeDetailDTO;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import java.util.List;

public interface EmployeeService {
    String MSG_ERROR_USERNAME_EXISTED = "Tên tài khoản đã tồn tại";
    String MSG_ERROR_INVALID_USERGROUP = "Nhóm người dùng không hợp lệ";
    String MSG_ERROR_CREATE_USER = "Tạo người dùng thất bại";
    String MSG_SUCCESS_CREATE_USER = "Tạo người dùng thành công";
    String MSG_SUCCESS_UPDATE_USER = "Cập nhật người dùng thành công";
    String MSG_ERROR_UPDATE_USER = "Cập nhật người dùng thất bại";
    String MSG_SUCCESS_RESET_PASSWORD = "Đặt lại mật khẩu thành công";
    String MSG_ERROR_RESET_PASSWORD = "Nhân viên không tồn tại: %s";
    String MSG_SUCCESS_UPDATE_ONLINE_STATUS = "Đã cập nhật trạng thái làm việc";
    String MSG_ERROR_UPDATE_ONLINE_STATUS = "Trùng trạng thái làm việc";
    String MSG_SUCCESS_DELETE_EMPLOYEE = "Xóa nhân viên thành công";
    String MSG_ERROR_EMPLOYEE_NOT_EXIST = "Lỗi nhân viên không tồn tại";
    String MSG_SUCCESS_GET_STATUS = "Truy vấn trạng thái làm việc thành công";
    String MSG_SUCCESS_GET_ALL_EMPLOYEES = "Truy vấn tất cả người dùng thành công";
    String MSG_OK_RESET_PASSWORD = "Đã tự đổi mật khẩu";
    String MSG_ERROR_WRONG_PASSWORD = "Sai mật khẩu hoặc tên đăng nhập";
    String MSG_SUCCESS_GET_EMPLOYEE = "Truy vấn người dùng thành công";
    String MSG_ERROR_GET_EMPLOYEE = "Truy vấn người dùng thất bại";

    APIResultSet<EmployeeDTO> createUser(EmployeeDTO employeeDTO);
    APIResultSet<EmployeeDetailDTO> updateUser(String username, EmployeeDTO employee);
    APIResultSet<EmployeeDTO> getUserByUsername(String username);
    APIResultSet<List<EmployeeDTO>> getAllUsers();
    APIResultSet<Void> changePassword(String username, String password, String newPassword);
    APIResultSet<List<EmployeeDashboardDTO>> getForDashboard();
    APIResultSet<StatusLogDTO> getLatestOnlineStatus(String username);
    APIResultSet<Void> updateOnlineStatus(StatusLogDTO logDTO);
    APIResultSet<Void> deleteByUsername(String username);
    APIResultSet<Void> resetPassword(ResetPasswordDTO resetPasswordDTO);
}

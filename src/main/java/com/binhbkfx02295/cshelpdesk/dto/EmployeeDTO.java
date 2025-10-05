package com.binhbkfx02295.cshelpdesk.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeDTO {
    private UserGroupDTO userGroup;
    private String name;

    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=,./;'\\[\\]<>:\"{}]).{8,}$",
            message="Mật khẩu phải có ít nhất 8 ký tự, chứa chữ hoa, chữ thường và ký tự đặc biệt (!@#$...).")
    private String password;
    private String description;
    private String email;
    private String phone;
    private Timestamp createdAt;
    private boolean isActive;
    private int failedLoginCount;
    private List<StatusLogDTO> statusLogs = new ArrayList<>();

}

package com.binhbkfx02295.cshelpdesk.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]{4,}$",
            message="Tên đăng nhập phải có ít nhất 4 ký tự, chỉ chứa chữ và số.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=,./;'\\[\\]<>:\"{}]).{8,}$",
            message="Mật khẩu phải có ít nhất 8 ký tự, chứa chữ hoa, chữ thường và ký tự đặc biệt (!@#$...).")
    private String defaultPassword;
}

package com.binhbkfx02295.cshelpdesk.authentication.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @Pattern(regexp = "^[a-zA-Z0-9._-]{4,}$", message = "Invalid username format")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=,./;'\\[\\]<>:\"{}]).{8,}$", message = "Invalid password format")
    private String password;
}

package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.config.UserPrincipal;
import com.binhbkfx02295.cshelpdesk.dto.LoginRequest;
import com.binhbkfx02295.cshelpdesk.dto.LoginResponse;
import com.binhbkfx02295.cshelpdesk.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto) throws AccountLockedException {
        LoginResponse response = authenticationService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(Map.of("user", user == null ? "no user" : user));
    }

}

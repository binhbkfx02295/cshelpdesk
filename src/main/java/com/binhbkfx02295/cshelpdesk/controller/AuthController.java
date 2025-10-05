package com.binhbkfx02295.cshelpdesk.controller;

import com.binhbkfx02295.cshelpdesk.dto.LoginRequest;
import com.binhbkfx02295.cshelpdesk.dto.LoginResponse;
import com.binhbkfx02295.cshelpdesk.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto) throws AccountLockedException {
        LoginResponse response = authenticationService.login(dto);
        return ResponseEntity.ok(response);
    }

}

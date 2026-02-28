package com.binhbkfx02295.cshelpdesk.authentication.controller;

import com.binhbkfx02295.cshelpdesk.authentication.dto.LoginRequestDTO;
import com.binhbkfx02295.cshelpdesk.authentication.dto.LoginResponseDTO;
import com.binhbkfx02295.cshelpdesk.authentication.service.AuthenticationService;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResponseEntityHelper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<APIResultSet<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return APIResponseEntityHelper.from(APIResultSet.badRequest("User already logged in", null));
        }
        return APIResponseEntityHelper.from(authenticationService.login(requestDTO));
    }

    @GetMapping("/logout")
    public ResponseEntity<APIResultSet<Void>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return APIResponseEntityHelper.from(authenticationService.logout(user.getUsername()));
        } else {
            return APIResponseEntityHelper.from(APIResultSet.ok());  
        }
    }

}

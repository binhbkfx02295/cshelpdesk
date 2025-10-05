package com.binhbkfx02295.cshelpdesk.config;

import com.binhbkfx02295.cshelpdesk.dto.LoginRequest;
import com.binhbkfx02295.cshelpdesk.dto.LoginResponse;
import com.binhbkfx02295.cshelpdesk.service.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountLockedException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {


    private final AuthenticationServiceImpl authenticationService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {


        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(rawPassword);

        LoginResponse response = null;
        try {
            response = authenticationService.login(loginRequest);
        } catch (AccountLockedException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        Set<GrantedAuthority> authorities = response.getEmployeeDTO().getUserGroup().getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + response.getEmployeeDTO().getUserGroup().getCode().toUpperCase()));

        UserPrincipal principal = UserPrincipal.builder()
                .username(response.getEmployeeDTO().getUsername())
                .fullName(response.getEmployeeDTO().getName())
                .description(response.getEmployeeDTO().getDescription())
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(principal,null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);

    }
}

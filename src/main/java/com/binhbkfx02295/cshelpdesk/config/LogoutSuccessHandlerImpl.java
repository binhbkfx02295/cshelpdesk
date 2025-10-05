package com.binhbkfx02295.cshelpdesk.config;

import com.binhbkfx02295.cshelpdesk.service.AuthenticationServiceImpl;
import com.binhbkfx02295.cshelpdesk.dto.EmployeeDTO;
import com.binhbkfx02295.cshelpdesk.service.EmployeeServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final AuthenticationServiceImpl authenticationService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        employeeDTO.setUsername(user.getUsername());

        authenticationService.logout(employeeDTO);
        SecurityContextHolder.getContext().setAuthentication(null);
        response.sendRedirect("/login?logout");
    }
}

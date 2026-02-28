package com.binhbkfx02295.cshelpdesk.infrastructure.security.config;

import com.binhbkfx02295.cshelpdesk.infrastructure.security.filter.AlreadyAuthenticatedFilter;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers(
                                                                "/api/auth/login")
                                                .permitAll()
                                                .requestMatchers("/api/report/**",
                                                                "/api/performance/**",
                                                                "/api/setting/**")
                                                .hasAuthority("SUPERVISOR")
                                                
                                                .requestMatchers("/api/**")
                                                .authenticated()
                                        .anyRequest().denyAll())
                                .exceptionHandling(handler -> {
                                        // write output as JSON when user is unauthorized as 401
                                        handler.authenticationEntryPoint((request, response, authException) -> {
                                                log.info("Unauthorized request: " + request.getRequestURI());
                                                log.info("authException: " + authException.getMessage());
                                                writeJSON(objectMapper, response,
                                                                APIResultSet.unauthorized("Chua dang nhap"));
                                        });

                                        // write output as JSON when access denied as 403
                                        handler.accessDeniedHandler((request, response, accessDeniedException) -> {
                                                log.info("Token: {}", SecurityContextHolder.getContext().getAuthentication());
                                                log.info("Access denied: " + request.getRequestURI());
                                                log.info("accessDeniedException: "
                                                                + accessDeniedException.getMessage());
                                                writeJSON(objectMapper, response,
                                                                APIResultSet.forbidden("Bi han che, vui long lien he quan tri vien."));
                                        });
                                })
                                .securityContext(config -> {
                                        config.requireExplicitSave(false);
                                })
                                .sessionManagement(session -> session
                                                .invalidSessionStrategy((request, response) -> {
                                                        log.info("Session invalid: " + request.getRequestURI());
                                                        writeJSON(objectMapper, response, APIResultSet
                                                                        .unauthorized("Session khong hop le"));
                                                })
                                                .maximumSessions(1) // chỉ 1 session
                                                .maxSessionsPreventsLogin(false));
                return http.build();
        }

        private <D> void writeJSON(ObjectMapper objectMapper, HttpServletResponse response, APIResultSet<D> body)
                        throws IOException, StreamWriteException, DatabindException {
                response.setContentType("application/json");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                objectMapper.writeValue(response.getOutputStream(), body);
        }


        @Bean
	public AuthenticationManager authenticationManager(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
                
		return new ProviderManager(authenticationProvider);
	}

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(Collections.singletonList("*")); // mới từ Spring Security 6+
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setAllowCredentials(true); // true nếu dùng cookie

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}

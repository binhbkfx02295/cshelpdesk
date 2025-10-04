package com.binhbkfx02295.cshelpdesk.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        logWarn(ex);
        return build(HttpStatus.BAD_REQUEST, "Validation failed", details, req);
    }




    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBind(BindException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        logWarn(ex);
        return build(HttpStatus.BAD_REQUEST, "Validation failed", details, req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest req) {
        logWarn(ex);
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of(), req);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, HttpServletRequest req) {
        logWarn(ex);
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), List.of(), req);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest req) {

        log.error("[UNCAUGHT EXCEPTION]", ExceptionUtils.getRootCause(ex));
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", List.of(), req);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message,
                                                List<String> details, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                req.getRequestURI(),
                message,
                details
        );


        return ResponseEntity.status(status).body(body);
    }

    private void logWarn(Throwable ex) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (auth != null ? auth.getPrincipal() : "anonymous");
        log.warn("[EXCEPTION] user={}, type={}, message={}",
                principal, ex.getClass().getSimpleName(), ex.getMessage(), ex);

    }

    public record ErrorResponse(
            String timestamp,
            int status,
            String error,
            String path,
            String message,
            List<String> details
    ) {}
}

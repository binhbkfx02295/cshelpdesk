package com.binhbkfx02295.cshelpdesk.authentication.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResponseEntityHelper;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIResultSet<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return APIResponseEntityHelper.from(APIResultSet.badRequest("Sai tên đăng nhập hoặc mật khẩu", null));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<APIResultSet<Void>> handleLockedException(LockedException ex) {
        return APIResponseEntityHelper.from(APIResultSet.badRequest("Tài khoản bị khóa", null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResultSet<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        return APIResponseEntityHelper.from(APIResultSet.badRequest("Sai tên đăng nhập hoặc mật khẩu", null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResultSet<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.warn("Request body missing or unreadable");
        return APIResponseEntityHelper
                .from(APIResultSet.badRequest("Thiếu dữ liệu đầu vào hoặc dữ liệu không hợp lệ", null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResultSet<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Dữ liệu không hợp lệ");
        return APIResponseEntityHelper.from(APIResultSet.badRequest(message, null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResultSet<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Unhandled error: ", ex);
        return APIResponseEntityHelper.from(APIResultSet.badRequest("Đã xảy ra lỗi không xác định", null));
    }

}

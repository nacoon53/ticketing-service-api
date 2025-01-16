package kr.hhplus.be.server.module.common.error.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(final ApiException e, HttpRequest request)  {

        log.warn("code: {}, message: {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode().getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e, HttpServletRequest request)  {
        log.info("restcontrolleradvice 탐");
        log.info(e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}

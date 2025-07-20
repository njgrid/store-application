package com.griddynamics.storeapplication.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(final UserNotFoundException userNotFoundException,
      final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), userNotFoundException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

}

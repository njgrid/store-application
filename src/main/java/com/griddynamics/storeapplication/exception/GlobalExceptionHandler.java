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

  @ExceptionHandler(ProductDoesNotExistException.class)
  public ResponseEntity<ErrorResponse> handleProductDoesNotExist(
      final ProductDoesNotExistException productDoesNotExistException, final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), productDoesNotExistException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ItemAlreadyInCartException.class)
  public ResponseEntity<ErrorResponse> handleItemAlreadyInCartException(
      final ItemAlreadyInCartException itemAlreadyInCartException, final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), itemAlreadyInCartException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientStockException(
      final InsufficientStockException insufficientStockException, final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), insufficientStockException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ItemNotPresentInCartException.class)
  public ResponseEntity<ErrorResponse> handleItemNotPresentInCartException(
      final ItemNotPresentInCartException itemNotPresentInCartException, final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), itemNotPresentInCartException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(CartNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCartNotFoundException(final CartNotFoundException cartNotFoundException,
      final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), cartNotFoundException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(PriceChangedException.class)
  public ResponseEntity<ErrorResponse> handlePriceChangedException(final PriceChangedException priceChangedException,
      final HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), priceChangedException.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

}

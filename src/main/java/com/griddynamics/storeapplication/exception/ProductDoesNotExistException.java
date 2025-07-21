package com.griddynamics.storeapplication.exception;

public class ProductDoesNotExistException extends RuntimeException {

  public ProductDoesNotExistException(final String message) {
    super(message);
  }

}


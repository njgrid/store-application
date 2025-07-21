package com.griddynamics.storeapplication.exception;

public class CartNotFoundException extends RuntimeException {

  public CartNotFoundException(final String message) {
    super(message);
  }

}

package com.griddynamics.storeapplication.exception;

public class ItemAlreadyInCartException extends RuntimeException {

  public ItemAlreadyInCartException(final String message) {
    super(message);
  }

}

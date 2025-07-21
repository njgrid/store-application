package com.griddynamics.storeapplication.exception;

public class ItemNotPresentInCartException extends RuntimeException {

  public ItemNotPresentInCartException(final String message) {
    super(message);
  }

}

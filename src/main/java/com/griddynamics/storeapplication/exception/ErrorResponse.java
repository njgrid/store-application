package com.griddynamics.storeapplication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

  private int    status;
  private String message;
  private String path;

}

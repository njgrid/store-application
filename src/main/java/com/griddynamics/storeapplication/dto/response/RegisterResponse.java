package com.griddynamics.storeapplication.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

  private int    status;
  private String message;

}

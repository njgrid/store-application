package com.griddynamics.storeapplication.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

  private String sessionId;

}

package com.griddynamics.storeapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveCartItemRequest {

  @NotNull
  private String sessionId;
  @NotNull
  private String productId;

}

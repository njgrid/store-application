package com.griddynamics.storeapplication.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifyCartItemRequest {

  @NotNull
  private String productId;

  @Min(1)
  private int quantity;

  @NotNull
  private String sessionId;

}

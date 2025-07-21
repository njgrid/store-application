package com.griddynamics.storeapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemRequest {

  @NotNull
  private String productId;

  @Min(1)
  private int quantity;

}


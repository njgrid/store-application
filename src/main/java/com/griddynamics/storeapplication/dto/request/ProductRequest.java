package com.griddynamics.storeapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {

  @NotNull
  private String     productId;
  private String     title;
  private int        available;
  private BigDecimal price;

}

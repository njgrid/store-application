package com.griddynamics.storeapplication.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {

  private String     productId;
  private String     title;
  private int        available;
  private BigDecimal price;

}

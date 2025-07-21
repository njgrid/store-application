package com.griddynamics.storeapplication.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {

  private int        ordinal;
  private String     title;
  private int        quantity;
  private BigDecimal subtotal;

}

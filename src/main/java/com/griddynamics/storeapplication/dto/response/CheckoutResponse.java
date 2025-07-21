package com.griddynamics.storeapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

  private boolean                  success;
  private String                   message;
  private BigDecimal               total;
  private List<CheckoutItemStatus> issues;

}

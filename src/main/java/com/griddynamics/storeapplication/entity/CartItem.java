package com.griddynamics.storeapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CartItem {

  private Product product;
  private int     quantity;

}

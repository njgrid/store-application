package com.griddynamics.storeapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {

  private Product product;
  private int     quantity;

}

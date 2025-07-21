package com.griddynamics.storeapplication.controller;

import com.griddynamics.storeapplication.dto.request.CartItemRequest;
import com.griddynamics.storeapplication.dto.request.ModifyCartItemRequest;
import com.griddynamics.storeapplication.dto.request.RemoveCartItemRequest;
import com.griddynamics.storeapplication.dto.response.CartResponse;
import com.griddynamics.storeapplication.service.CartService;
import com.griddynamics.storeapplication.util.CommonUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonUtils.BASE_API + CartController.API_CART)
public class CartController {

  public static final  String API_CART                             = "/cart";
  private static final String API_ADD                              = "/add";
  private static final String API_DELETE                           = "/delete";
  private static final String API_MODIFY                           = "/modify";
  private static final String HEADER_SESSION_ID                    = "X-Session-Id";
  private static final String ITEM_ADDITION_SUCCESS_MESSAGE        = "Item added to cart";
  private static final String ITEM_REMOVAL_SUCCESS_MESSAGE         = "Item removed from cart";
  private static final String ITEM_QUANTITY_UPDATE_SUCCESS_MESSAGE = "Cart item quantity updated successfully";

  @Autowired
  private CartService cartService;

  @PostMapping(API_ADD)
  public String addItemToCart(@RequestBody @Valid final CartItemRequest cartItemRequest,
      @RequestHeader(HEADER_SESSION_ID) final String sessionId) {
    cartService.addItem(sessionId, cartItemRequest);
    return ITEM_ADDITION_SUCCESS_MESSAGE;
  }

  @GetMapping
  public CartResponse getCartDetails(@RequestHeader(HEADER_SESSION_ID) final String sessionId) {
    return cartService.getCartDetails(sessionId);
  }

  @DeleteMapping(API_DELETE)
  public String removeItemFromUserCart(@RequestBody @Valid final RemoveCartItemRequest removeCartItemRequest) {
    cartService.removeItemFromUserCart(removeCartItemRequest);
    return ITEM_REMOVAL_SUCCESS_MESSAGE;
  }

  @PutMapping(API_MODIFY)
  public String modifyCartItem(@Valid @RequestBody final ModifyCartItemRequest modifyCartItemRequest) {
    cartService.modifyItemQuantity(modifyCartItemRequest);
    return ITEM_QUANTITY_UPDATE_SUCCESS_MESSAGE;
  }

}

package com.griddynamics.storeapplication.service;

import com.griddynamics.storeapplication.dto.request.CartItemRequest;
import com.griddynamics.storeapplication.dto.request.ModifyCartItemRequest;
import com.griddynamics.storeapplication.dto.request.RemoveCartItemRequest;
import com.griddynamics.storeapplication.dto.response.CartItemResponse;
import com.griddynamics.storeapplication.dto.response.CartResponse;
import com.griddynamics.storeapplication.dto.response.ProductResponse;
import com.griddynamics.storeapplication.entity.CartItem;
import com.griddynamics.storeapplication.entity.Product;
import com.griddynamics.storeapplication.exception.CartNotFoundException;
import com.griddynamics.storeapplication.exception.InsufficientStockException;
import com.griddynamics.storeapplication.exception.ItemAlreadyInCartException;
import com.griddynamics.storeapplication.exception.ItemNotPresentInCartException;
import com.griddynamics.storeapplication.exception.ProductDoesNotExistException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

  private static final String PRODUCT_DOES_NOT_EXIST   = "product with the given id does not exist";
  private static final String ITEM_ALREADY_IN_CART     = "Item already in cart. Modify instead.";
  private static final String INSUFFICIENT_QUANTITY    = "Insufficient quantity in store";
  private static final String ITEM_NOT_PRESENT_IN_CART = "Item with the given product id is not present in cart";
  private static final String CART_NOT_FOUND           = "Cart not found for given session";


  private final Map<String, List<CartItem>> carts = new ConcurrentHashMap<>();

  @Autowired
  private ProductService productService;

  public void addItem(final String sessionId, final CartItemRequest cartItemRequest) {
    Product product = Optional
        .ofNullable(productService.getProductById(cartItemRequest.getProductId()))
        .map(this::toProduct)
        .orElseThrow(() -> new ProductDoesNotExistException(PRODUCT_DOES_NOT_EXIST));

    if (product.getAvailable() < cartItemRequest.getQuantity()) {
      throw new InsufficientStockException(INSUFFICIENT_QUANTITY);
    }

    List<CartItem> cartItems = carts.computeIfAbsent(sessionId, key -> new ArrayList<>());
    synchronized (cartItems) {
      Optional<CartItem> existingItem = cartItems
          .stream()
          .filter(cartItem -> isRequestCartItemAlreadyInCart(cartItem, cartItemRequest.getProductId()))
          .findFirst();

      if (existingItem.isPresent()) {
        throw new ItemAlreadyInCartException(ITEM_ALREADY_IN_CART);
      }

      cartItems.add(new CartItem(product, cartItemRequest.getQuantity()));
    }

  }

  public CartResponse getCartDetails(final String sessionId) {
    List<CartItem> cartItems = carts.computeIfAbsent(sessionId, key -> Collections.synchronizedList(new ArrayList<>()));

    BigDecimal totalSubtotal = BigDecimal.ZERO;
    int ordinalCount = 0;

    List<CartItemResponse> cartItemResponsesList = new ArrayList<>();

    synchronized (cartItems) {
      for (CartItem cartItem : cartItems) {
        BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
        BigDecimal price = cartItem.getProduct().getPrice();
        BigDecimal cartItemTotalSubTotal = price.multiply(quantity);

        CartItemResponse response = CartItemResponse
            .builder()
            .ordinal(ordinalCount++)
            .quantity(cartItem.getQuantity())
            .title(cartItem.getProduct().getTitle())
            .subtotal(cartItemTotalSubTotal)
            .build();

        cartItemResponsesList.add(response);
        totalSubtotal = totalSubtotal.add(cartItemTotalSubTotal);
      }
    }

    return CartResponse.builder().cart(cartItemResponsesList).totalSubtotal(totalSubtotal).build();
  }

  public void removeItemFromUserCart(final RemoveCartItemRequest request) {
    final String sessionId = request.getSessionId();
    final String productIdToRemove = request.getProductId();

    List<CartItem> cartItems = carts.get(sessionId);
    if (CollectionUtils.isEmpty(cartItems)) {
      throw new CartNotFoundException(CART_NOT_FOUND);
    }

    synchronized (cartItems) {
      boolean removed = cartItems.removeIf(cartItem -> isRequestCartItemAlreadyInCart(cartItem, productIdToRemove));

      if (!removed) {
        throw new ItemNotPresentInCartException(ITEM_NOT_PRESENT_IN_CART);
      }
    }
  }

  public void modifyItemQuantity(final ModifyCartItemRequest modifyCartItemRequest) {
    List<CartItem> cartItems = carts.get(modifyCartItemRequest.getSessionId());
    if (CollectionUtils.isEmpty(cartItems)) {
      throw new CartNotFoundException(CART_NOT_FOUND);
    }

    Product product = Optional
        .ofNullable(productService.getProductById(modifyCartItemRequest.getProductId()))
        .map(this::toProduct)
        .orElseThrow(() -> new ProductDoesNotExistException(PRODUCT_DOES_NOT_EXIST));

    if (product.getAvailable() < modifyCartItemRequest.getQuantity()) {
      throw new InsufficientStockException(INSUFFICIENT_QUANTITY);
    }

    synchronized (cartItems) {
      Optional<CartItem> existingItem = cartItems
          .stream()
          .filter(cartItem -> isRequestCartItemAlreadyInCart(cartItem, modifyCartItemRequest.getProductId()))
          .findFirst();

      if (existingItem.isPresent()) {
        existingItem.get().setQuantity(modifyCartItemRequest.getQuantity());
      } else {
        throw new ItemNotPresentInCartException(ITEM_NOT_PRESENT_IN_CART);
      }
    }
  }

  private boolean isRequestCartItemAlreadyInCart(final CartItem cartItem, final String requestedProductId) {
    return Optional
        .ofNullable(cartItem)
        .map(CartItem::getProduct)
        .map(Product::getProductId)
        .filter(productId -> StringUtils.equals(productId, requestedProductId))
        .isPresent();
  }

  private Product toProduct(final ProductResponse productResponse) {
    return Product
        .builder()
        .productId(productResponse.getProductId())
        .title(productResponse.getTitle())
        .available(productResponse.getAvailable())
        .price(productResponse.getPrice())
        .build();
  }

}

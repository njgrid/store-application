package com.griddynamics.storeapplication.service;

import com.griddynamics.storeapplication.dto.response.CartItemResponse;
import com.griddynamics.storeapplication.dto.response.CartResponse;
import com.griddynamics.storeapplication.dto.response.CheckoutItemStatus;
import com.griddynamics.storeapplication.dto.response.CheckoutResponse;
import com.griddynamics.storeapplication.dto.response.ProductResponse;
import com.griddynamics.storeapplication.entity.CartItem;
import com.griddynamics.storeapplication.entity.Product;
import com.griddynamics.storeapplication.exception.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {

  private static final String CART_NOT_FOUND        = "Cart not found for session %s";
  private static final String PRODUCT_OUT_OF_STOCK  = "Only these many items in stock: ";
  private static final String PRICE_CHANGED_MESSAGE = "Price has changed. Latest Price: ";
  private static final String CHECKOUT_FAILED       = "Checkout failed due to item conflicts";
  private static final String ORDER_PLACED_SUCCESS  = "Order placed successfully";

  @Autowired
  private CartService    cartService;
  @Autowired
  private ProductService productService;

  public CheckoutResponse checkout(final String sessionId) {
    List<CartItem> cartItems = getCartItemsFromSession(sessionId);

    List<CheckoutItemStatus> issues = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (CartItem cartItem : cartItems) {
      Product latestProduct = getLatestProduct(cartItem);

      if (priceChanged(cartItem, latestProduct)) {
        issues.add(new CheckoutItemStatus(cartItem.getProduct().getTitle(),
            PRICE_CHANGED_MESSAGE + latestProduct.getPrice()));
        continue;
      }

      if (outOfStock(cartItem, latestProduct)) {
        issues.add(new CheckoutItemStatus(cartItem.getProduct().getTitle(),
            PRODUCT_OUT_OF_STOCK + latestProduct.getAvailable()));
        continue;
      }

      total = total.add(latestProduct.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
    }

    if (!issues.isEmpty()) {
      return CheckoutResponse.builder().success(false).issues(issues).message(CHECKOUT_FAILED).build();
    }

    cartItems.forEach(item -> productService.reduceStock(item.getProduct().getProductId(), item.getQuantity()));

    cartService.clearCart(sessionId);

    return CheckoutResponse.builder().success(true).message(ORDER_PLACED_SUCCESS).total(total).build();
  }

  private List<CartItem> getCartItemsFromSession(final String sessionId) {
    return Optional
        .ofNullable(cartService.getCartDetails(sessionId))
        .map(CartResponse::getCart)
        .map(cart -> cart.stream().map(this::toCartItem).toList())
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new CartNotFoundException(String.format(CART_NOT_FOUND, sessionId)));
  }

  private Product getLatestProduct(CartItem cartItem) {
    return Optional
        .ofNullable(cartItem)
        .map(CartItem::getProduct)
        .map(Product::getProductId)
        .map(productService::getProductById)
        .map(this::toProduct)
        .orElseThrow();
  }

  private boolean priceChanged(final CartItem cartItem, final Product latestProduct) {
    return latestProduct.getPrice().compareTo(cartItem.getProduct().getPrice()) != 0;
  }

  private boolean outOfStock(final CartItem cartItem, final Product latestProduct) {
    return cartItem.getQuantity() > latestProduct.getAvailable();
  }

  private CartItem toCartItem(final CartItemResponse response) {
    Product product = Product
        .builder()
        .productId(response.getProductId())
        .title(response.getTitle())
        .price(response.getPrice())
        .available(response.getQuantity())
        .build();

    return CartItem.builder().quantity(response.getQuantity()).product(product).build();
  }

  private Product toProduct(final ProductResponse response) {
    return Product
        .builder()
        .productId(response.getProductId())
        .title(response.getTitle())
        .available(response.getAvailable())
        .price(response.getPrice())
        .build();
  }

}

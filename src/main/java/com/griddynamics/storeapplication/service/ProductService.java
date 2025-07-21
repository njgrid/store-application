package com.griddynamics.storeapplication.service;

import com.griddynamics.storeapplication.dto.request.ProductRequest;
import com.griddynamics.storeapplication.dto.response.ProductResponse;
import com.griddynamics.storeapplication.entity.Product;
import com.griddynamics.storeapplication.exception.InsufficientStockException;
import com.griddynamics.storeapplication.exception.ProductDoesNotExistException;
import com.griddynamics.storeapplication.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

  public static final  String PRODUCT_DOES_NOT_EXIST = "product with the given id does not exist";
  private static final String INSUFFICIENT_QUANTITY  = "Insufficient quantity in store for the given product";

  @Autowired
  private ProductRepository productRepository;

  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream().map(this::toProductResponse).toList();
  }

  public ProductResponse getProductById(final String productId) {
    return productRepository.findByProductId(productId).map(this::toProductResponse).orElse(null);
  }

  public ProductResponse addProduct(final ProductRequest productRequest) {
    Product incomingProduct = toProduct(productRequest);

    Product productToSave = productRepository.findByProductId(incomingProduct.getProductId()).map(existingProduct -> {
      existingProduct.setAvailable(existingProduct.getAvailable() + incomingProduct.getAvailable());
      return existingProduct;
    }).orElse(incomingProduct);

    Product savedProduct = productRepository.save(productToSave);
    return toProductResponse(savedProduct);
  }

  public void reduceStock(final String productId, final int quantity) {
    Product product = productRepository
        .findByProductId(productId)
        .orElseThrow(() -> new ProductDoesNotExistException(PRODUCT_DOES_NOT_EXIST));

    if (product.getAvailable() < quantity) {
      throw new InsufficientStockException(INSUFFICIENT_QUANTITY);
    }

    product.setAvailable(product.getAvailable() - quantity);
    productRepository.save(product);
  }

  private ProductResponse toProductResponse(final Product product) {
    return ProductResponse
        .builder()
        .productId(product.getProductId())
        .title(product.getTitle())
        .available(product.getAvailable())
        .price(product.getPrice())
        .build();
  }

  private Product toProduct(final ProductRequest productRequest) {
    return Product
        .builder()
        .productId(productRequest.getProductId())
        .title(productRequest.getTitle())
        .available(productRequest.getAvailable())
        .price(productRequest.getPrice())
        .build();
  }

}

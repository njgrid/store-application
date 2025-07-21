package com.griddynamics.storeapplication.service;

import com.griddynamics.storeapplication.dto.request.ProductRequest;
import com.griddynamics.storeapplication.dto.response.ProductResponse;
import com.griddynamics.storeapplication.entity.Product;
import com.griddynamics.storeapplication.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

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

package com.griddynamics.storeapplication.controller;

import com.griddynamics.storeapplication.dto.request.ProductRequest;
import com.griddynamics.storeapplication.dto.response.ProductResponse;
import com.griddynamics.storeapplication.exception.ProductDoesNotExistException;
import com.griddynamics.storeapplication.service.ProductService;
import com.griddynamics.storeapplication.util.CommonUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(CommonUtils.BASE_API + ProductController.API_PRODUCT)
public class ProductController {

  public static final String API_PRODUCT            = "/products";
  public static final String PRODUCT_DOES_NOT_EXIST = "product with the given id does not exist";

  @Autowired
  private ProductService productService;

  @GetMapping
  public List<ProductResponse> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/{productId}")
  public ProductResponse getProductById(@PathVariable("productId") final String productId) {
    return Optional
        .ofNullable(productService.getProductById(productId))
        .orElseThrow(() -> new ProductDoesNotExistException(PRODUCT_DOES_NOT_EXIST));
  }

  //to have it ACCESS BASED (allow only admin)
  @PostMapping
  public ProductResponse addProduct(@Valid @RequestBody final ProductRequest productRequest) {
    return productService.addProduct(productRequest);
  }

}

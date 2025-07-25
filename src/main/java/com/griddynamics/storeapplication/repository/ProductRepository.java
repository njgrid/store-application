package com.griddynamics.storeapplication.repository;

import com.griddynamics.storeapplication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByProductId(final String productId);

}

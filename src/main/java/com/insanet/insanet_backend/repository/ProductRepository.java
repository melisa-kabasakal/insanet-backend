package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

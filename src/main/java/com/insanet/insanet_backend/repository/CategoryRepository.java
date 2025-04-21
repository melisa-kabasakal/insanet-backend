package com.insanet.insanet_backend.repository;

import com.insanet.insanet_backend.entity.Category;
import com.insanet.insanet_backend.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, CategoryType> {
}

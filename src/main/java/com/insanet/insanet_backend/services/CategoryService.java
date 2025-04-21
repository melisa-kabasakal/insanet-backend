package com.insanet.insanet_backend.services;

import com.insanet.insanet_backend.entity.Category;
import com.insanet.insanet_backend.enums.CategoryType;
import com.insanet.insanet_backend.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        if (categoryRepository.count() == 0) {
            Arrays.stream(CategoryType.values()).forEach(type -> {
                Category category = new Category();
                category.setId(type);
                category.setName(type.getDisplayName());
                categoryRepository.save(category);
            });
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

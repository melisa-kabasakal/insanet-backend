package com.insanet.insanet_backend.controller;

import com.insanet.insanet_backend.entity.Category;
import com.insanet.insanet_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Map<String, List<Category>>> getAllCategories() {
        return ResponseEntity.ok(Map.of("categories", categoryService.getAllCategories()));
    }
}

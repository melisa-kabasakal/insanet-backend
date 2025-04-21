package com.insanet.insanet_backend.entity;

import com.insanet.insanet_backend.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories", schema = "insanet")
public class Category {
    @Id
    @Enumerated(EnumType.STRING)
    private CategoryType id;

    private String name;
}

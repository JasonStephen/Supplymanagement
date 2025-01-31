package com.jason.supplymanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-20
 */

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private ProductCategory category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // 修改为 BigDecimal 类型

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + (category != null ? category.getCategoryId() : null) +
                ", price=" + price +
                '}';
    }
}
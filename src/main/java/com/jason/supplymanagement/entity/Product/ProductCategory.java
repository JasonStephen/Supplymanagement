package com.jason.supplymanagement.entity.Product;

import jakarta.persistence.*;

@Entity
@Table(name = "Product_Category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_category_id", referencedColumnName = "category_id", nullable = true)
    private ProductCategory parentCategory;

    // 默认构造函数
    public ProductCategory() {
    }

    // 带参数的构造函数
    public ProductCategory(int categoryId) {
        this.categoryId = categoryId;
    }


    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ProductCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ProductCategory parentCategory) {
        this.parentCategory = parentCategory;
    }
}
package com.jason.supplymanagement.dto;

import java.math.BigDecimal;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-02-11
 */

public class ProductDTO {
    private int productId;
    private String name;
    private String categoryName;
    private BigDecimal price;
    private String photo;

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

    public String getCategoryName() {
        return categoryName != null ? categoryName : "默认类别"; // 避免 null
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

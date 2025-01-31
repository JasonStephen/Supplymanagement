package com.jason.supplymanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    private int productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "alert_threshold", nullable = false)
    private int alertThreshold;

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(int alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", alertThreshold=" + alertThreshold +
                '}';
    }
}
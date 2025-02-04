package com.jason.supplymanagement.entity;

import java.io.Serializable;
import java.util.Objects;

public class ProductComponentId implements Serializable {

    private int product;
    private int component;

    public ProductComponentId() {}

    public ProductComponentId(int product, int component) {
        this.product = product;
        this.component = component;
    }

    // Getters and Setters
    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getComponent() {
        return component;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductComponentId that = (ProductComponentId) o;
        return product == that.product && component == that.component;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, component);
    }
}
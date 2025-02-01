package com.jason.supplymanagement.entity;

import java.io.Serializable;
import java.util.Objects;

public class ProductMaterialId implements Serializable {

    private int product;
    private int material;

    public ProductMaterialId() {}

    public ProductMaterialId(int product, int material) {
        this.product = product;
        this.material = material;
    }

    // Getters and Setters
    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMaterialId that = (ProductMaterialId) o;
        return product == that.product && material == that.material;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, material);
    }
}
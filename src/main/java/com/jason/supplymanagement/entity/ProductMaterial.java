package com.jason.supplymanagement.entity;

import jakarta.persistence.*;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-20
 */

@Entity
@Table(name = "Product_Material")
@IdClass(ProductMaterialId.class) // 使用复合主键类
public class ProductMaterial {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id", nullable = false)
    private Material material;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ProductMaterial{" +
                "productId=" + (product != null ? product.getProductId() : null) +
                ", materialId=" + (material != null ? material.getMaterialId() : null) +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}
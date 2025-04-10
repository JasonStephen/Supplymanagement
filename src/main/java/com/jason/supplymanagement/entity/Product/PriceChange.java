package com.jason.supplymanagement.entity.Product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * @author : Jason Stephen
 * @date :Created in 2025-04-10
 */

@Entity
@Table(name = "Price_Change")
public class PriceChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_id")
    private Long changeId;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name = "old_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal oldPrice;
    @Column(name = "new_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal newPrice;
    @Column(name = "change_date", nullable = false)
    private LocalDateTime changeDate;
    // Getters and Setters
    public Long getChangeId() {
        return changeId;
    }
    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public BigDecimal getOldPrice() {
        return oldPrice;
    }
    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }
    public BigDecimal getNewPrice() {
        return newPrice;
    }
    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }
    public LocalDateTime getChangeDate() {
        return changeDate;
    }
    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }
    @Override
    public String toString() {
        return "PriceChange{" +
                "changeId=" + changeId +
                ", productId=" + (product != null ? product.getProductId() : null) +
                ", oldPrice=" + oldPrice +
                ", newPrice=" + newPrice +
                ", changeDate=" + changeDate +
                '}';
    }
}
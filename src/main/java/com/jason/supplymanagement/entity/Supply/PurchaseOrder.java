package com.jason.supplymanagement.entity.Supply;

import com.jason.supplymanagement.entity.Product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "Purchase_Order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_id")
    private int purchaseOrderId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "product_id", insertable = false, updatable = false)
    private int productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    private PurchaseContract purchaseContract;

    // Getters and Setters
    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseContract getPurchaseContract() {
        return purchaseContract;
    }

    public void setPurchaseContract(PurchaseContract purchaseContract) {
        this.purchaseContract = purchaseContract;
    }
}
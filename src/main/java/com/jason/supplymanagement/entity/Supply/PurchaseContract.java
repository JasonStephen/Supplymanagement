package com.jason.supplymanagement.entity.Supply;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-20
 */

@Entity
@Table(name = "Purchase_Contract")
public class PurchaseContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private int contractId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "contract_content", nullable = false, columnDefinition = "TEXT")
    private String contractContent;

    @Column(name = "signing_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime signingDate;

    @Column(name = "expiry_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expiryDate;

    // Getters and Setters
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getContractContent() {
        return contractContent;
    }

    public void setContractContent(String contractContent) {
        this.contractContent = contractContent;
    }

    public LocalDateTime getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(LocalDateTime signingDate) {
        this.signingDate = signingDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "PurchaseContract{" +
                "contractId=" + contractId +
                ", supplierId=" + (supplier != null ? supplier.getSupplierId() : null) +
                ", contractContent='" + contractContent + '\'' +
                ", signingDate=" + signingDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
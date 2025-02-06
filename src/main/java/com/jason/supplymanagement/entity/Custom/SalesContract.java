// SalesContract.java
package com.jason.supplymanagement.entity.Custom;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sales_Contract")
public class SalesContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private int contractId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "contract_content", nullable = false, columnDefinition = "TEXT")
    private String contractContent;

    @Column(name = "signing_date", nullable = true)
    private LocalDateTime signingDate;

    @Column(name = "expiry_date", nullable = true)
    private LocalDateTime expiryDate;

    // Getters and Setters
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
}
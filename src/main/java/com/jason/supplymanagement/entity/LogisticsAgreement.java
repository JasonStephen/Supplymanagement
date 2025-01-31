package com.jason.supplymanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Logistics_Agreement")
public class LogisticsAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agreement_id")
    private int agreementId;

    @Column(name = "logistics_company_id")
    private int logisticsCompanyId;

    @Column(name = "agreement_content", nullable = false, columnDefinition = "TEXT")
    private String agreementContent;

    @Column(name = "signing_date", nullable = false)
    private LocalDate signingDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    // Getters and Setters
    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public int getLogisticsCompanyId() {
        return logisticsCompanyId;
    }

    public void setLogisticsCompanyId(int logisticsCompanyId) {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    public String getAgreementContent() {
        return agreementContent;
    }

    public void setAgreementContent(String agreementContent) {
        this.agreementContent = agreementContent;
    }

    public LocalDate getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(LocalDate signingDate) {
        this.signingDate = signingDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "LogisticsAgreement{" +
                "agreementId=" + agreementId +
                ", logisticsCompanyId=" + logisticsCompanyId +
                ", agreementContent='" + agreementContent + '\'' +
                ", signingDate=" + signingDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
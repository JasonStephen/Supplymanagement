package com.jason.supplymanagement.dto;

import java.time.LocalDateTime;

public class ProcessOrderRequest {
    private int logisticsCompanyId;
    private String contractContent;
    private LocalDateTime signingTime;
    private LocalDateTime expiryTime;
    private int deliveryTime;
    private int userId;

    public int getLogisticsCompanyId() {
        return logisticsCompanyId;
    }

    public void setLogisticsCompanyId(int logisticsCompanyId) {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    public String getContractContent() {
        return contractContent;
    }

    public void setContractContent(String contractContent) {
        this.contractContent = contractContent;
    }

    public LocalDateTime getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(LocalDateTime signingTime) {
        this.signingTime = signingTime;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
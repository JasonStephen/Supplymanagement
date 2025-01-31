package com.jason.supplymanagement.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-20
 */

@Entity
@Table(name = "Logistics_Order")
public class LogisticsOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logistics_order_id")
    private int logisticsOrderId;

    @Column(name = "purchase_order_id")
    private int purchaseOrderId;

    @Column(name = "sales_order_id")
    private int salesOrderId;

    @Column(name = "logistics_company_id")
    private int logisticsCompanyId;

    @Column(name = "shipping_date", nullable = false)
    private Date shippingDate;

    @Column(name = "estimated_arrival_date", nullable = false)
    private Date estimatedArrivalDate;

    @Column(name = "actual_arrival_date")
    private Date actualArrivalDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", insertable = false, updatable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "sales_order_id", insertable = false, updatable = false)
    private SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name = "logistics_company_id", insertable = false, updatable = false)
    private LogisticsCompany logisticsCompany;

    // Getters and Setters
    public int getLogisticsOrderId() {
        return logisticsOrderId;
    }

    public void setLogisticsOrderId(int logisticsOrderId) {
        this.logisticsOrderId = logisticsOrderId;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public int getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(int salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public int getLogisticsCompanyId() {
        return logisticsCompanyId;
    }

    public void setLogisticsCompanyId(int logisticsCompanyId) {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Date getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public void setEstimatedArrivalDate(Date estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public Date getActualArrivalDate() {
        return actualArrivalDate;
    }

    public void setActualArrivalDate(Date actualArrivalDate) {
        this.actualArrivalDate = actualArrivalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LogisticsOrder{" +
                "logisticsOrderId=" + logisticsOrderId +
                ", purchaseOrderId=" + purchaseOrderId +
                ", salesOrderId=" + salesOrderId +
                ", logisticsCompanyId=" + logisticsCompanyId +
                ", shippingDate=" + shippingDate +
                ", estimatedArrivalDate=" + estimatedArrivalDate +
                ", actualArrivalDate=" + actualArrivalDate +
                ", status='" + status + '\'' +
                '}';
    }
}
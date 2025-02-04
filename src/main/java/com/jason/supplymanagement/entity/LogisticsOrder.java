package com.jason.supplymanagement.entity;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "agreement_id", referencedColumnName = "agreement_id")
    private LogisticsAgreement logisticsAgreement;

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

    public LogisticsAgreement getLogisticsAgreement() {
        return logisticsAgreement;
    }

    public void setLogisticsAgreement(LogisticsAgreement logisticsAgreement) {
        this.logisticsAgreement = logisticsAgreement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public LogisticsCompany getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(LogisticsCompany logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }
}
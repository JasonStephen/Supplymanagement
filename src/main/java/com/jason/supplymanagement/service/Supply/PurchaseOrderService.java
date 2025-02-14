package com.jason.supplymanagement.service.Supply;

import com.jason.supplymanagement.entity.Supply.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseOrder> getAllPurchaseOrders();
    PurchaseOrder getPurchaseOrderById(int id);
    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);
    PurchaseOrder updatePurchaseOrder(int id, PurchaseOrder purchaseOrder);
    void deletePurchaseOrder(int id);

    List<PurchaseOrder> getNewPurchaseOrders(int i);
}
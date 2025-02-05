package com.jason.supplymanagement.service.Custom;

import com.jason.supplymanagement.entity.Custom.SalesOrder;

import java.util.List;

public interface SalesOrderService {
    List<SalesOrder> getAllSalesOrders();
    SalesOrder getSalesOrderById(int id);
    SalesOrder createSalesOrder(SalesOrder salesOrder);
    SalesOrder updateSalesOrder(int id, SalesOrder salesOrder);
    void deleteSalesOrder(int id);
}
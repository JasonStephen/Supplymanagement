package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping("/all-orders")
    public List<Object> getAllOrders() {
        List<Object> allOrders = new ArrayList<>();
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();
        allOrders.addAll(purchaseOrders);
        allOrders.addAll(salesOrders);
        return allOrders;
    }
}
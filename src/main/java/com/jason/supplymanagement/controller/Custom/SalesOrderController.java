package com.jason.supplymanagement.controller.Custom;

import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderService.getAllSalesOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getSalesOrderById(@PathVariable int id) {
        SalesOrder salesOrder = salesOrderService.getSalesOrderById(id);
        if (salesOrder != null) {
            return ResponseEntity.ok(salesOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public SalesOrder createSalesOrder(@RequestBody SalesOrder salesOrder) {
        return salesOrderService.createSalesOrder(salesOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrder> updateSalesOrder(@PathVariable int id, @RequestBody SalesOrder salesOrder) {
        SalesOrder updatedSalesOrder = salesOrderService.updateSalesOrder(id, salesOrder);
        if (updatedSalesOrder != null) {
            return ResponseEntity.ok(updatedSalesOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable int id) {
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.noContent().build();
    }
}
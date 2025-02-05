package com.jason.supplymanagement.controller.Supply;

import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable int id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (purchaseOrder != null) {
            return ResponseEntity.ok(purchaseOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@PathVariable int id, @RequestBody PurchaseOrder purchaseOrder) {
        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updatePurchaseOrder(id, purchaseOrder);
        if (updatedPurchaseOrder != null) {
            return ResponseEntity.ok(updatedPurchaseOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable int id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.noContent().build();
    }
}
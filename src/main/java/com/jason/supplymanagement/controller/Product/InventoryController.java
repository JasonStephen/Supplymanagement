package com.jason.supplymanagement.controller.Product;

import com.jason.supplymanagement.entity.Product.Inventory;
import com.jason.supplymanagement.entity.Product.InventoryAdjustment;
import com.jason.supplymanagement.service.Product.InventoryService;
import com.jason.supplymanagement.service.Product.InventoryAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryAdjustmentService inventoryAdjustmentService;

    @GetMapping("/{productId}")
    public Inventory getInventoryByProductId(@PathVariable int productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PutMapping("/{productId}/alert-threshold")
    public Inventory updateInventoryAlertThreshold(@PathVariable int productId, @RequestParam int alertThreshold) {
        return inventoryService.updateInventoryAlertThreshold(productId, alertThreshold);
    }

    @PutMapping("/{productId}/adjust")
    public Inventory adjustInventory(@PathVariable int productId, @RequestParam int quantity) {
        return inventoryService.adjustInventory(productId, quantity);
    }

    @GetMapping("/{productId}/adjustments")
    public List<InventoryAdjustment> getAdjustmentsByProductId(@PathVariable int productId) {
        return inventoryAdjustmentService.getAdjustmentsByProductId(productId);
    }

    @PostMapping("/adjustments")
    public InventoryAdjustment createAdjustment(@RequestBody InventoryAdjustment adjustment) {
        return inventoryAdjustmentService.createAdjustment(adjustment);
    }

    @PostMapping
    public Inventory createInventory(@RequestBody Inventory inventory) {
        return inventoryService.createInventory(inventory);
    }
}
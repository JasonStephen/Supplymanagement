package com.jason.supplymanagement.service.Product;

import com.jason.supplymanagement.entity.Product.InventoryAdjustment;

import java.util.List;

public interface InventoryAdjustmentService {
    List<InventoryAdjustment> getAdjustmentsByProductId(int productId);
    InventoryAdjustment createAdjustment(InventoryAdjustment adjustment);
}
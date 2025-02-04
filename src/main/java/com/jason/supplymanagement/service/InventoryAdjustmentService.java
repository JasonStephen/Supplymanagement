package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.InventoryAdjustment;

import java.util.List;

public interface InventoryAdjustmentService {
    List<InventoryAdjustment> getAdjustmentsByProductId(int productId);
    InventoryAdjustment createAdjustment(InventoryAdjustment adjustment);
}
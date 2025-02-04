package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.Inventory;

public interface InventoryService {
    Inventory getInventoryByProductId(int productId);
    Inventory updateInventoryAlertThreshold(int productId, int alertThreshold);
    Inventory adjustInventory(int productId, int quantity);
    Inventory createInventory(Inventory inventory);
}
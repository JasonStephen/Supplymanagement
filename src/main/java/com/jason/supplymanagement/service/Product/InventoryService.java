package com.jason.supplymanagement.service.Product;

import com.jason.supplymanagement.entity.Product.Inventory;

public interface InventoryService {
    Inventory getInventoryByProductId(int productId);
    Inventory updateInventoryAlertThreshold(int productId, int alertThreshold);
    Inventory adjustInventory(int productId, int quantity);
    Inventory createInventory(Inventory inventory);
    void updateInventory(Inventory inventory);
}
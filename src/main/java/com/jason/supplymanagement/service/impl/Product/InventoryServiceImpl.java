package com.jason.supplymanagement.service.impl.Product;

import com.jason.supplymanagement.dao.Product.InventoryDAO;
import com.jason.supplymanagement.entity.Product.Inventory;
import com.jason.supplymanagement.service.Product.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDAO inventoryDAO;

    @Override
    public Inventory getInventoryByProductId(int productId) {
        return inventoryDAO.findByProductId(productId);
    }

    @Override
    public Inventory updateInventoryAlertThreshold(int productId, int alertThreshold) {
        Inventory inventory = inventoryDAO.findByProductId(productId);
        if (inventory != null) {
            inventory.setAlertThreshold(alertThreshold);
            return inventoryDAO.save(inventory);
        }
        return null;
    }

    @Override
    public Inventory adjustInventory(int productId, int quantity) {
        Inventory inventory = inventoryDAO.findByProductId(productId);
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            return inventoryDAO.save(inventory);
        }
        return null;
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        return inventoryDAO.save(inventory);
    }
}
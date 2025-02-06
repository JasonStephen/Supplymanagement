package com.jason.supplymanagement.service.impl.Product;

import com.jason.supplymanagement.dao.Product.InventoryAdjustmentDAO;
import com.jason.supplymanagement.dao.Users.UserDAO;
import com.jason.supplymanagement.entity.Product.InventoryAdjustment;
import com.jason.supplymanagement.service.Product.InventoryAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {

    @Autowired
    private InventoryAdjustmentDAO inventoryAdjustmentDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<InventoryAdjustment> getAdjustmentsByProductId(int productId) {
        return inventoryAdjustmentDAO.findByProductId(productId);
    }

    @Override
    public InventoryAdjustment createAdjustment(InventoryAdjustment adjustment) {
        // Validate User ID
        if (!userDAO.existsById(adjustment.getUserId())) {
            throw new IllegalArgumentException("User ID does not exist");
        }
        return inventoryAdjustmentDAO.save(adjustment);
    }
}
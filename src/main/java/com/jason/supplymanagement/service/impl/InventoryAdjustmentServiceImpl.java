package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.InventoryAdjustmentDAO;
import com.jason.supplymanagement.entity.InventoryAdjustment;
import com.jason.supplymanagement.service.InventoryAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {

    @Autowired
    private InventoryAdjustmentDAO inventoryAdjustmentDAO;

    @Override
    public List<InventoryAdjustment> getAdjustmentsByProductId(int productId) {
        return inventoryAdjustmentDAO.findByProductId(productId);
    }

    @Override
    public InventoryAdjustment createAdjustment(InventoryAdjustment adjustment) {
        return inventoryAdjustmentDAO.save(adjustment);
    }
}
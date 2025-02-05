package com.jason.supplymanagement.service.impl.Supply;

import com.jason.supplymanagement.dao.Supply.PurchaseOrderDAO;
import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;

    @Override
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderDAO.findAll();
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(int id) {
        return purchaseOrderDAO.findById(id).orElse(null);
    }

    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderDAO.save(purchaseOrder);
    }

    @Override
    public PurchaseOrder updatePurchaseOrder(int id, PurchaseOrder purchaseOrder) {
        if (purchaseOrderDAO.existsById(id)) {
            purchaseOrder.setPurchaseOrderId(id);
            return purchaseOrderDAO.save(purchaseOrder);
        }
        return null;
    }

    @Override
    public void deletePurchaseOrder(int id) {
        purchaseOrderDAO.deleteById(id);
    }
}
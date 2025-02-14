package com.jason.supplymanagement.service.impl.Supply;

import com.jason.supplymanagement.dao.Supply.PurchaseOrderDAO;
import com.jason.supplymanagement.dao.Supply.PurchaseContractDAO;
import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.entity.Supply.PurchaseContract;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;

    @Autowired
    private PurchaseContractDAO purchaseContractDAO;

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
        PurchaseOrder purchaseOrder = purchaseOrderDAO.findById(id).orElse(null);
        if (purchaseOrder != null) {
            PurchaseContract purchaseContract = purchaseOrder.getPurchaseContract();
            if (purchaseContract != null) {
                purchaseContractDAO.deleteById(purchaseContract.getContractId());
            }
            purchaseOrderDAO.deleteById(id);
        }
    }

    @Override
    public List<PurchaseOrder> getNewPurchaseOrders(int limit) {
        // 获取 status 为 0 的最新采购订单
        return purchaseOrderDAO.findAll(Sort.by(Sort.Direction.DESC, "purchaseOrderId")).stream()
                .filter(order -> "0".equals(order.getStatus()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
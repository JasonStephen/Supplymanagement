package com.jason.supplymanagement.service.impl.Logistics;

import com.jason.supplymanagement.dao.Custom.SalesOrderDAO;
import com.jason.supplymanagement.dao.Logistics.LogisticsOrderDAO;
import com.jason.supplymanagement.dao.Supply.PurchaseOrderDAO;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsOrderServiceImpl implements LogisticsOrderService {

    @Autowired
    private LogisticsOrderDAO logisticsOrderDAO;

    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;

    @Autowired
    private SalesOrderDAO salesOrderDAO;

    @Override
    public List<LogisticsOrder> getAllLogisticsOrders() {
        return logisticsOrderDAO.findAll();
    }

    @Override
    public LogisticsOrder getLogisticsOrderById(int id) {
        return logisticsOrderDAO.findById(id).orElse(null);
    }

    @Override
    public LogisticsOrder createLogisticsOrder(LogisticsOrder logisticsOrder) {
        if (logisticsOrder.getPurchaseOrderId() != null && purchaseOrderDAO.existsById(logisticsOrder.getPurchaseOrderId())) {
            // Proceed with creating the logistics order
        } else if (logisticsOrder.getSalesOrderId() != null && salesOrderDAO.existsById(logisticsOrder.getSalesOrderId())) {
            // Proceed with creating the logistics order
        } else {
            throw new IllegalArgumentException("The given id must not be null");
        }
        return logisticsOrderDAO.save(logisticsOrder);
    }

    @Override
    public LogisticsOrder updateLogisticsOrder(int id, LogisticsOrder logisticsOrder) {
        if (logisticsOrderDAO.existsById(id)) {
            logisticsOrder.setLogisticsOrderId(id);
            return logisticsOrderDAO.save(logisticsOrder);
        }
        return null;
    }

    @Override
    public void deleteLogisticsOrder(int id) {
        logisticsOrderDAO.deleteById(id);
    }

    @Override
    public LogisticsOrder getLogisticsOrderBySalesOrderId(int id) {
        List<LogisticsOrder> orders = logisticsOrderDAO.findBySalesOrderId(id);
        if (orders != null && !orders.isEmpty()) {
            return orders.get(0);
        }
        return null;
    }
}
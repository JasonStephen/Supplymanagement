package com.jason.supplymanagement.service.impl.Custom;

import com.jason.supplymanagement.dao.Custom.SalesOrderDAO;
import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    private SalesOrderDAO salesOrderDAO;

    @Override
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderDAO.findAll();
    }

    @Override
    public SalesOrder getSalesOrderById(int id) {
        return salesOrderDAO.findById(id).orElse(null);
    }

    @Override
    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        return salesOrderDAO.save(salesOrder);
    }

    @Override
    public SalesOrder updateSalesOrder(int id, SalesOrder salesOrder) {
        if (salesOrderDAO.existsById(id)) {
            salesOrder.setSalesOrderId(id);
            return salesOrderDAO.save(salesOrder);
        }
        return null;
    }

    @Override
    public void deleteSalesOrder(int id) {
        salesOrderDAO.deleteById(id);
    }
}
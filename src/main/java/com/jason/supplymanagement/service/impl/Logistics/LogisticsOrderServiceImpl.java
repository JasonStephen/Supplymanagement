package com.jason.supplymanagement.service.impl.Logistics;

import com.jason.supplymanagement.dao.Logistics.LogisticsOrderDAO;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsOrderServiceImpl implements LogisticsOrderService {

    @Autowired
    private LogisticsOrderDAO logisticsOrderDAO;

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
}
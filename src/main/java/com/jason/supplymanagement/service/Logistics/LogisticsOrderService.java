package com.jason.supplymanagement.service.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;

import java.util.List;

public interface LogisticsOrderService {
    List<LogisticsOrder> getAllLogisticsOrders();
    LogisticsOrder getLogisticsOrderById(int id);
    LogisticsOrder createLogisticsOrder(LogisticsOrder logisticsOrder);
    LogisticsOrder updateLogisticsOrder(int id, LogisticsOrder logisticsOrder);
    void deleteLogisticsOrder(int id);
}
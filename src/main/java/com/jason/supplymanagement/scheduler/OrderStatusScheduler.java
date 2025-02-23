package com.jason.supplymanagement.scheduler;

import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.entity.Custom.SalesContract;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import com.jason.supplymanagement.service.Custom.SalesContractService;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusScheduler.class);

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private SalesContractService salesContractService;

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Scheduled(fixedRate = 10000)
    public void checkAndUpdateOrderStatus() {
        LocalDateTime now = LocalDateTime.now();

        // 检查并更新销售订单状态
        List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();
        logger.info("Total sales orders retrieved: {}", salesOrders.size());

        for (SalesOrder salesOrder : salesOrders) {
            logger.info("Processing sales order ID: {}", salesOrder.getSalesOrderId());
            if (salesOrder.getStatus() == 2) {
                SalesContract contract = salesOrder.getContract();
                if (contract == null) {
                    contract = salesContractService.getSalesContractById(salesOrder.getContractId());
                    salesOrder.setContract(contract);
                }
                if (contract != null) {
                    logger.debug("Sales order ID: {} has contract ID: {} with expiry date: {}", salesOrder.getSalesOrderId(), contract.getContractId(), contract.getExpiryDate());
                    LocalDateTime expiryDate = contract.getExpiryDate();
                    if (expiryDate == null) {
                        logger.warn("Expiry date is null for order ID: {}, skipping", salesOrder.getSalesOrderId());
                        continue;
                    }
                    if (expiryDate.isBefore(now)) {
                        logger.info("Expiry date is before now for order ID: {}, updating status to 3", salesOrder.getSalesOrderId());
                        salesOrder.setStatus(3);
                        salesOrderService.updateSalesOrder(salesOrder.getSalesOrderId(), salesOrder);
                    } else {
                        logger.info("Expiry date is not before now for order ID: {}. Expiry date: {}", salesOrder.getSalesOrderId(), expiryDate);
                    }
                } else {
                    logger.warn("Sales order ID: {} has no contract associated", salesOrder.getSalesOrderId());
                }
            } else {
                logger.info("Sales order status is not 2 for order ID: {}", salesOrder.getSalesOrderId());
            }
        }

        // 确认并更新物流订单状态
        List<LogisticsOrder> logisticsOrders = logisticsOrderService.getAllLogisticsOrders();
        for (LogisticsOrder logisticsOrder : logisticsOrders) {
            if (logisticsOrder.getStatus().equals("0") && logisticsOrder.getLogisticsAgreement().getExpiryDate().isBefore(now)) {
                if (logisticsOrder.getPurchaseOrderId() != null) {
                    logisticsOrder.setStatus("2");
                } else if (logisticsOrder.getSalesOrderId() != null) {
                    logisticsOrder.setStatus("1");
                }
                logisticsOrderService.updateLogisticsOrder(logisticsOrder.getLogisticsOrderId(), logisticsOrder);
            }
        }
    }
}
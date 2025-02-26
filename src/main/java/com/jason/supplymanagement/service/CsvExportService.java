package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.service.Custom.CustomerService;
import com.jason.supplymanagement.service.Product.ProductService;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class CsvExportService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;

    public byte[] exportLogisticsOrdersToCsv(List<LogisticsOrder> orders) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        CSVWriter csvWriter = new CSVWriter(writer);

        // 写入CSV头部
        String[] header = {
                "物流ID", "订单类型", "客户/供应商", "产品", "数量", "单价", "总价", "物流状态", "订单状态"
        };
        csvWriter.writeNext(header);

        // 写入数据行
        for (LogisticsOrder order : orders) {
            String orderType = order.getPurchaseOrderId() != null ? "采购订单" : "销售订单";
            String customerOrSupplierName = "";
            String productName = "";
            String quantity = "";
            String unitPrice = "";
            String totalPrice = "";
            String orderStatus = "";

            if (order.getPurchaseOrderId() != null) {
                customerOrSupplierName = order.getPurchaseOrder().getSupplier() != null ? order.getPurchaseOrder().getSupplier().getName() : "N/A";
                productName = order.getPurchaseOrder().getProduct() != null ? order.getPurchaseOrder().getProduct().getName() : "N/A";
                quantity = String.valueOf(order.getPurchaseOrder().getQuantity());
                unitPrice = String.valueOf(order.getPurchaseOrder().getUnitPrice());
                totalPrice = String.valueOf(order.getPurchaseOrder().getTotalPrice());
                orderStatus = mapPurchaseOrderStatus(order.getPurchaseOrder().getStatus());
            } else if (order.getSalesOrderId() != null) {
                SalesOrder salesOrder = order.getSalesOrder();
                if (salesOrder != null) {
                    salesOrder.setCustomer(customerService.getCustomerById(salesOrder.getCustomerId()));
                    salesOrder.setProduct(productService.getProductById(salesOrder.getProductId()));
                }
                customerOrSupplierName = salesOrder.getCustomer() != null ? salesOrder.getCustomer().getName() : "N/A";
                productName = salesOrder.getProduct() != null ? salesOrder.getProduct().getName() : "N/A";
            }

            String[] data = {
                    String.valueOf(order.getLogisticsOrderId()),
                    orderType,
                    customerOrSupplierName,
                    productName,
                    quantity,
                    unitPrice,
                    totalPrice,
                    mapLogisticsStatus(order.getStatus()),
                    orderStatus
            };
            csvWriter.writeNext(data);
        }

        csvWriter.close();
        writer.close();
        outputStream.close();

        return outputStream.toByteArray();
    }

    private String mapLogisticsStatus(String status) {
        switch (status) {
            case "0": return "正在运输";
            case "1": return "已完成";
            case "2": return "待签收";
            default: return "未知状态";
        }
    }

    private String mapSalesOrderStatus(int status) {
        switch (status) {
            case 0: return "待接单";
            case 1: return "待签署";
            case 2: return "进行中";
            case 3: return "已完成";
            default: return "未知状态";
        }
    }

    private String mapPurchaseOrderStatus(String status) {
        switch (status) {
            case "0": return "待接单";
            case "1": return "待签署";
            case "2": return "进行中";
            case "3": return "已完成";
            default: return "未知状态";
        }
    }
}

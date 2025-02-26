package com.jason.supplymanagement.controller.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.service.CsvExportService;
import com.jason.supplymanagement.service.Custom.CustomerService;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import com.jason.supplymanagement.service.Product.ProductService;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/logistics-orders")
public class LogisticsOrderController {

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CsvExportService csvExportService;

    @GetMapping
    public List<LogisticsOrder> getAllLogisticsOrders() {
        List<LogisticsOrder> logisticsOrders = logisticsOrderService.getAllLogisticsOrders();
        for (LogisticsOrder order : logisticsOrders) {
            if (order.getPurchaseOrderId() != null) {
                order.setPurchaseOrder(purchaseOrderService.getPurchaseOrderById(order.getPurchaseOrderId()));
            } else if (order.getSalesOrderId() != null) {
                order.setSalesOrder(salesOrderService.getSalesOrderById(order.getSalesOrderId()));
                if (order.getSalesOrder() != null) {
                    order.getSalesOrder().setCustomer(customerService.getCustomerById(order.getSalesOrder().getCustomerId()));
                    order.getSalesOrder().setProduct(productService.getProductById(order.getSalesOrder().getProductId()));
                }
            }
        }
        return logisticsOrders;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogisticsOrder> getLogisticsOrderById(@PathVariable int id) {
        LogisticsOrder logisticsOrder = logisticsOrderService.getLogisticsOrderById(id);
        if (logisticsOrder != null) {
            if (logisticsOrder.getPurchaseOrderId() != null) {
                logisticsOrder.setPurchaseOrder(purchaseOrderService.getPurchaseOrderById(logisticsOrder.getPurchaseOrderId()));
            } else if (logisticsOrder.getSalesOrderId() != null) {
                logisticsOrder.setSalesOrder(salesOrderService.getSalesOrderById(logisticsOrder.getSalesOrderId()));
                if (logisticsOrder.getSalesOrder() != null) {
                    logisticsOrder.getSalesOrder().setCustomer(customerService.getCustomerById(logisticsOrder.getSalesOrder().getCustomerId()));
                    logisticsOrder.getSalesOrder().setProduct(productService.getProductById(logisticsOrder.getSalesOrder().getProductId()));
                }
            }
            return ResponseEntity.ok(logisticsOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public LogisticsOrder createLogisticsOrder(@RequestBody LogisticsOrder logisticsOrder) {
        return logisticsOrderService.createLogisticsOrder(logisticsOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogisticsOrder> updateLogisticsOrder(@PathVariable int id, @RequestBody LogisticsOrder logisticsOrder) {
        LogisticsOrder updatedLogisticsOrder = logisticsOrderService.updateLogisticsOrder(id, logisticsOrder);
        if (updatedLogisticsOrder != null) {
            return ResponseEntity.ok(updatedLogisticsOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogisticsOrder(@PathVariable int id) {
        logisticsOrderService.deleteLogisticsOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/confirm-receipt")
    public ResponseEntity<Void> confirmReceipt(@PathVariable int id) {
        logisticsOrderService.confirmReceipt(id);
        return ResponseEntity.ok().build();
    }

    //将订单信息导出为CSV文件
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportLogisticsOrdersToCsv() throws IOException {
        List<LogisticsOrder> orders = logisticsOrderService.getAllLogisticsOrders();
        byte[] csvBytes = csvExportService.exportLogisticsOrdersToCsv(orders);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "logistics_orders.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}
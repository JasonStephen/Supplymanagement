package com.jason.supplymanagement.controller.Custom;

import com.jason.supplymanagement.entity.Custom.SalesContract;
import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.service.Custom.SalesContractService;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import com.jason.supplymanagement.service.Custom.CustomerService;
import com.jason.supplymanagement.service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private SalesContractService salesContractService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<SalesOrder> getAllSalesOrders() {
        List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();
        for (SalesOrder order : salesOrders) {
            order.setCustomer(customerService.getCustomerById(order.getCustomerId()));
            order.setProduct(productService.getProductById(order.getProductId()));
            order.setContract(salesContractService.getSalesContractById(order.getContractId()));
        }
        return salesOrders;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getSalesOrderById(@PathVariable int id) {
        SalesOrder salesOrder = salesOrderService.getSalesOrderById(id);
        if (salesOrder != null) {
            salesOrder.setCustomer(customerService.getCustomerById(salesOrder.getCustomerId()));
            salesOrder.setProduct(productService.getProductById(salesOrder.getProductId()));
            salesOrder.setContract(salesContractService.getSalesContractById(salesOrder.getContractId()));
            return ResponseEntity.ok(salesOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SalesOrder> createSalesOrder(@RequestBody SalesOrder salesOrder) {
        SalesContract salesContract = new SalesContract();
        salesContract.setCustomerId(salesOrder.getCustomerId());
        salesContract.setContractContent(salesOrder.getContract().getContractContent());

        salesContract = salesContractService.createSalesContract(salesContract);
        salesOrder.setContractId(salesContract.getContractId());

        salesOrder.setTotalPrice(salesOrder.getQuantity() * salesOrder.getUnitPrice());
        salesOrder.setStatus(0);

        salesOrder = salesOrderService.createSalesOrder(salesOrder);
        return ResponseEntity.ok(salesOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrder> updateSalesOrder(@PathVariable int id, @RequestBody SalesOrder salesOrder) {
        SalesOrder existingOrder = salesOrderService.getSalesOrderById(id);
        if (existingOrder != null) {
            existingOrder.setCustomerId(salesOrder.getCustomerId());
            existingOrder.setProductId(salesOrder.getProductId());
            existingOrder.setQuantity(salesOrder.getQuantity());
            existingOrder.setUnitPrice(salesOrder.getUnitPrice());
            existingOrder.setTotalPrice(salesOrder.getQuantity() * salesOrder.getUnitPrice());

            SalesContract salesContract = salesContractService.getSalesContractById(existingOrder.getContractId());
            if (salesContract != null) {
                salesContract.setCustomerId(salesOrder.getCustomerId());
                salesContract.setContractContent(salesOrder.getContract().getContractContent());
                salesContractService.updateSalesContract(salesContract.getContractId(), salesContract);
            }

            salesOrderService.updateSalesOrder(id, existingOrder);
            return ResponseEntity.ok(existingOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable int id) {
        SalesOrder salesOrder = salesOrderService.getSalesOrderById(id);
        if (salesOrder != null) {
            salesContractService.deleteSalesContract(salesOrder.getContractId());
            salesOrderService.deleteSalesOrder(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
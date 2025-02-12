package com.jason.supplymanagement.controller.Custom;

import com.jason.supplymanagement.dto.ProcessOrderRequest;

import com.jason.supplymanagement.entity.Custom.SalesContract;
import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.entity.Logistics.LogisticsAgreement;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.entity.Product.Inventory;
import com.jason.supplymanagement.entity.Product.InventoryAdjustment;
import com.jason.supplymanagement.entity.Users.User;

import com.jason.supplymanagement.service.Custom.SalesContractService;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import com.jason.supplymanagement.service.Custom.CustomerService;
import com.jason.supplymanagement.service.Logistics.LogisticsAgreementService;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import com.jason.supplymanagement.service.Product.InventoryAdjustmentService;
import com.jason.supplymanagement.service.Product.InventoryService;
import com.jason.supplymanagement.service.Product.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryAdjustmentService inventoryAdjustmentService;

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Autowired
    private LogisticsAgreementService logisticsAgreementService;



    @GetMapping
    public List<SalesOrder> getAllSalesOrders(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        List<SalesOrder> orders = salesOrderService.getAllSalesOrders()
                .stream()
                .filter(order -> order.getStatus() == 0) // 只返回 status=0 的订单
                .peek(order -> {
                    order.setCustomer(customerService.getCustomerById(order.getCustomerId()));
                    order.setProduct(productService.getProductById(order.getProductId()));
                    order.setContract(salesContractService.getSalesContractById(order.getContractId()));
                })
                .collect(Collectors.toList());

        // 搜索功能：按产品名过滤
        if (productName != null && !productName.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> order.getProduct().getName().toLowerCase().contains(productName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // 筛选功能：按客户ID和产品ID过滤
        if (customerId != null) {
            orders = orders.stream()
                    .filter(order -> order.getCustomerId() == customerId)
                    .collect(Collectors.toList());
        }

        if (productId != null) {
            orders = orders.stream()
                    .filter(order -> order.getProductId() == productId)
                    .collect(Collectors.toList());
        }

        // 排序功能
        if (sortBy != null && sortOrder != null) {
            Comparator<SalesOrder> comparator = null;
            switch (sortBy) {
                case "unitPrice":
                    comparator = Comparator.comparing(SalesOrder::getUnitPrice);
                    break;
                case "totalPrice":
                    comparator = Comparator.comparing(SalesOrder::getTotalPrice);
                    break;
                case "salesOrderId":
                    comparator = Comparator.comparing(SalesOrder::getSalesOrderId);
                    break;
                default:
                    break;
            }
            if (comparator != null) {
                if (sortOrder.equalsIgnoreCase("desc")) {
                    comparator = comparator.reversed();
                }
                orders = orders.stream().sorted(comparator).collect(Collectors.toList());
            }
        }

        return orders;
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
        salesOrder.setStatus(0); //[状态变化]创建订单，状态改为0

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

    @PostMapping("/{id}/process")
    public ResponseEntity<?> processSalesOrder(@PathVariable int id, @RequestBody ProcessOrderRequest request, HttpSession session) {
        SalesOrder salesOrder = salesOrderService.getSalesOrderById(id);
        if (salesOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sales order not found");
        }

        Inventory inventory = inventoryService.getInventoryByProductId(salesOrder.getProductId());
        if (inventory == null || inventory.getQuantity() < salesOrder.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient product inventory");
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be logged in to process sales order");
        }
        request.setUserId(user.getUserId());

        LocalDateTime signingTime = LocalDateTime.now();
        request.setSigningTime(signingTime);

        // Calculate expiry date
        LocalDateTime expiryDate = signingTime.plusSeconds(request.getDeliveryTime());

        // Update sales order status
        salesOrder.setStatus(2);
        salesOrderService.updateSalesOrder(id, salesOrder);

        // Update sales contract
        SalesContract salesContract = salesContractService.getSalesContractById(salesOrder.getContractId());
        if (salesContract != null) {
            salesContract.setCustomerId(salesOrder.getCustomerId());
            salesContract.setContractContent(request.getContractContent());
            salesContract.setSigningDate(signingTime);
            salesContract.setExpiryDate(expiryDate); // Set calculated expiry date
            salesContractService.updateSalesContract(salesContract.getContractId(), salesContract);
        } else {
            // If no existing contract, create a new one
            salesContract = new SalesContract();
            salesContract.setCustomerId(salesOrder.getCustomerId());
            salesContract.setContractContent(request.getContractContent());
            salesContract.setSigningDate(signingTime);
            salesContract.setExpiryDate(expiryDate); // Set calculated expiry date
            salesContractService.createSalesContract(salesContract);
            salesOrder.setContractId(salesContract.getContractId());
        }

        // Create logistics agreement
        LogisticsAgreement logisticsAgreement = new LogisticsAgreement();
        logisticsAgreement.setLogisticsCompanyId(request.getLogisticsCompanyId());
        logisticsAgreement.setAgreementContent(request.getContractContent());
        logisticsAgreement.setSigningDate(signingTime);
        logisticsAgreement.setExpiryDate(expiryDate); // Set calculated expiry date
        logisticsAgreementService.createLogisticsAgreement(logisticsAgreement);

        // Create logistics order
        LogisticsOrder logisticsOrder = new LogisticsOrder();
        logisticsOrder.setSalesOrderId(id);
        logisticsOrder.setLogisticsCompanyId(request.getLogisticsCompanyId());
        logisticsOrder.setStatus("0"); //[状态变化]创建物流订单，状态改为0
        logisticsOrder.setLogisticsAgreement(logisticsAgreement);
        logisticsOrderService.createLogisticsOrder(logisticsOrder);

        // Adjust inventory
        inventory.setQuantity(inventory.getQuantity() - salesOrder.getQuantity());
        inventoryService.updateInventory(inventory);

        // Create inventory adjustment
        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setProductId(salesOrder.getProductId());
        adjustment.setQuantity(-salesOrder.getQuantity());
        adjustment.setReason("Sold " + salesOrder.getQuantity() + " products");
        adjustment.setUserId(request.getUserId());
        inventoryAdjustmentService.createAdjustment(adjustment);

        return ResponseEntity.ok().build();
    }

    // 由于销售不需要确认收货，直接跳过签收的过程
    @PostMapping("/{id}/confirm-receipt")
    public ResponseEntity<?> confirmReceipt(@PathVariable int id) {
        SalesOrder salesOrder = salesOrderService.getSalesOrderById(id);
        if (salesOrder == null) {
            return ResponseEntity.notFound().build();
        }

        salesOrder.setStatus(3);
        salesOrderService.updateSalesOrder(id, salesOrder);

        LogisticsOrder logisticsOrder = logisticsOrderService.getLogisticsOrderBySalesOrderId(id);
        if (logisticsOrder != null) {
            logisticsOrder.setStatus("1"); //[状态变化]自动确认收货，状态改为1
            logisticsOrderService.updateLogisticsOrder(logisticsOrder.getLogisticsOrderId(), logisticsOrder);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/process")
    public String processSalesOrder(@RequestBody InventoryAdjustment adjustment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new IllegalArgumentException("User must be logged in to process sales order");
        }
        adjustment.setUserId(user.getUserId());
        inventoryAdjustmentService.createAdjustment(adjustment);
        return "Sales order processed successfully";
    }


}
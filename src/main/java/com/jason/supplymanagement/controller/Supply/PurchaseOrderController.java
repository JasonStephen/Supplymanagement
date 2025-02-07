package com.jason.supplymanagement.controller.Supply;

import com.jason.supplymanagement.dto.ProcessOrderRequest;
import com.jason.supplymanagement.dto.PurchaseOrderRequest;

import com.jason.supplymanagement.entity.Logistics.LogisticsAgreement;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.entity.Product.Inventory;
import com.jason.supplymanagement.entity.Product.InventoryAdjustment;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Supply.PurchaseContract;
import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.entity.Supply.Supplier;
import com.jason.supplymanagement.entity.Users.User;

import com.jason.supplymanagement.service.Logistics.LogisticsAgreementService;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import com.jason.supplymanagement.service.Product.InventoryAdjustmentService;
import com.jason.supplymanagement.service.Product.InventoryService;
import com.jason.supplymanagement.service.Product.ProductService;
import com.jason.supplymanagement.service.Supply.PurchaseContractService;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import com.jason.supplymanagement.service.Supply.SupplierService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Autowired
    private LogisticsAgreementService logisticsAgreementService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryAdjustmentService inventoryAdjustmentService;

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable int id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (purchaseOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(purchaseOrder);
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderRequest requestDto) {
        Supplier supplier = supplierService.getSupplierById(requestDto.getSupplierId());
        if (supplier == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Product product = productService.getProductById(requestDto.getProductId());
        if (product == null) {
            return ResponseEntity.badRequest().body(null);
        }

        PurchaseContract purchaseContract = new PurchaseContract();
        purchaseContract.setSupplier(supplier);
        purchaseContract.setContractContent(requestDto.getContractContent());
        purchaseContract = purchaseContractService.createPurchaseContract(purchaseContract);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setProduct(product);
        purchaseOrder.setQuantity(requestDto.getQuantity());
        purchaseOrder.setUnitPrice(requestDto.getUnitPrice());
        purchaseOrder.setTotalPrice(requestDto.getUnitPrice() * requestDto.getQuantity());
        purchaseOrder.setPurchaseContract(purchaseContract);
        purchaseOrder.setStatus("0");

        PurchaseOrder createdOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
            @PathVariable int id,
            @RequestBody PurchaseOrderRequest requestDto
    ) {
        PurchaseOrder existingOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }

        Supplier supplier = supplierService.getSupplierById(requestDto.getSupplierId());
        Product product = productService.getProductById(requestDto.getProductId());
        if (supplier == null || product == null) {
            return ResponseEntity.badRequest().build();
        }

        existingOrder.setSupplier(supplier);
        existingOrder.setProduct(product);
        existingOrder.setQuantity(requestDto.getQuantity());
        existingOrder.setUnitPrice(requestDto.getUnitPrice());
        existingOrder.setTotalPrice(requestDto.getUnitPrice() * requestDto.getQuantity());

        PurchaseContract existingContract = existingOrder.getPurchaseContract();
        if (existingContract == null) {
            existingContract = new PurchaseContract();
            existingContract.setSupplier(supplier);
        }
        existingContract.setContractContent(requestDto.getContractContent());
        if (existingContract.getContractId() == 0) {
            purchaseContractService.createPurchaseContract(existingContract);
        } else {
            purchaseContractService.updatePurchaseContract(existingContract.getContractId(), existingContract);
        }
        existingOrder.setPurchaseContract(existingContract);

        PurchaseOrder updatedOrder = purchaseOrderService.updatePurchaseOrder(id, existingOrder);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable int id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (purchaseOrder != null) {
            if (purchaseOrder.getPurchaseContract() != null) {
                purchaseContractService.deletePurchaseContract(purchaseOrder.getPurchaseContract().getContractId());
            }
            purchaseOrderService.deletePurchaseOrder(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<?> processPurchaseOrder(@PathVariable int id, @RequestBody ProcessOrderRequest request, HttpSession session) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (purchaseOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Purchase order not found");
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be logged in to process purchase order");
        }
        request.setUserId(user.getUserId());

        LocalDateTime signingTime = LocalDateTime.now();
        request.setSigningTime(signingTime);

        // 计算采购的产品的到达时间
        LocalDateTime expiryDate = signingTime.plusSeconds(request.getDeliveryTime());

        // 更新采购订单状态
        purchaseOrder.setStatus("2");//[状态变化]有人采购了，状态为2
        purchaseOrderService.updatePurchaseOrder(id, purchaseOrder);

        // 创建采购合同
        PurchaseContract purchaseContract = purchaseOrder.getPurchaseContract();
        if (purchaseContract != null) {
            purchaseContract.setSigningDate(signingTime);
            purchaseContract.setExpiryDate(expiryDate); // Set calculated expiry date
            purchaseContractService.updatePurchaseContract(purchaseContract.getContractId(), purchaseContract);
        } else {
            // 如果没有找到对应的采购合同，可以选择抛出异常或进行其他处理
            throw new IllegalStateException("No purchase contract associated with the purchase order");
        }

        // 创建物流合同
        LogisticsAgreement logisticsAgreement = new LogisticsAgreement();
        logisticsAgreement.setLogisticsCompanyId(request.getLogisticsCompanyId());
        logisticsAgreement.setAgreementContent(request.getContractContent());
        logisticsAgreement.setSigningDate(signingTime);
        logisticsAgreement.setExpiryDate(expiryDate); // Set calculated expiry date
        logisticsAgreementService.createLogisticsAgreement(logisticsAgreement);

        // 创建物流订单
        LogisticsOrder logisticsOrder = new LogisticsOrder();
        logisticsOrder.setPurchaseOrderId(id);
        logisticsOrder.setLogisticsCompanyId(request.getLogisticsCompanyId());
        logisticsOrder.setStatus("0"); //[状态变化]运输订单创建，状态为0
        logisticsOrder.setLogisticsAgreement(logisticsAgreement);
        logisticsOrderService.createLogisticsOrder(logisticsOrder);

        // 更新库存
        Inventory inventory = inventoryService.getInventoryByProductId(purchaseOrder.getProduct().getProductId());
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setProductId(purchaseOrder.getProduct().getProductId());
            inventory.setQuantity(purchaseOrder.getQuantity());
            inventoryService.createInventory(inventory);
        } else {
            inventory.setQuantity(inventory.getQuantity() + purchaseOrder.getQuantity());
            inventoryService.updateInventory(inventory);
        }

        // 创建库存调整
        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setProductId(purchaseOrder.getProduct().getProductId());
        adjustment.setQuantity(purchaseOrder.getQuantity());
        adjustment.setReason("Purchased " + purchaseOrder.getQuantity() + " products");
        adjustment.setUserId(request.getUserId());
        inventoryAdjustmentService.createAdjustment(adjustment);

        return ResponseEntity.ok().build();
    }
}
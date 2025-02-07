package com.jason.supplymanagement.controller.Supply;

import com.jason.supplymanagement.dto.PurchaseOrderRequest;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Supply.PurchaseContract;
import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.entity.Supply.Supplier;
import com.jason.supplymanagement.service.Product.ProductService;
import com.jason.supplymanagement.service.Supply.PurchaseContractService;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import com.jason.supplymanagement.service.Supply.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Product product = productService.getProductById(requestDto.getProductId());
        if (supplier == null || product == null) {
            return ResponseEntity.badRequest().build();
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
}
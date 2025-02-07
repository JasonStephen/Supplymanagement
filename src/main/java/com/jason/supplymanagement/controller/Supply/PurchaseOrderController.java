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

    /**
     * 获取所有采购订单
     */
    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        for (PurchaseOrder order : purchaseOrders) {
            if (order.getPurchaseContract() != null) {
                PurchaseContract contract = purchaseContractService.getPurchaseContractById(
                        order.getPurchaseContract().getContractId()
                );
                order.setPurchaseContract(contract);
            }
        }
        return purchaseOrders;
    }

    /**
     * 根据 ID 获取采购订单
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable int id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (purchaseOrder != null) {
            if (purchaseOrder.getPurchaseContract() != null) {
                PurchaseContract contract = purchaseContractService.getPurchaseContractById(
                        purchaseOrder.getPurchaseContract().getContractId()
                );
                purchaseOrder.setPurchaseContract(contract);
            }
            return ResponseEntity.ok(purchaseOrder);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 创建采购订单并同时创建采购合同
     * 参考 SalesOrderController 中的 createSalesOrder，统一从 JSON body 获取合同内容 (contractContent) 等信息
     */
    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderRequest requestDto) {

        // 先在数据库中查找出对应的供应商和产品
        Supplier supplier = supplierService.getSupplierById(requestDto.getSupplierId());
        Product product = productService.getProductById(requestDto.getProductId());
        if (supplier == null || product == null) {
            // 若supplier或product不存在，视业务需要返回错误或抛异常
            return ResponseEntity.badRequest().build();
        }

        // 创建采购合同
        PurchaseContract purchaseContract = new PurchaseContract();
        purchaseContract.setSupplier(supplier);
        purchaseContract.setContractContent(requestDto.getContractContent());
        // 若需要签署时间、到期时间等字段，也可在这里设置
        purchaseContract = purchaseContractService.createPurchaseContract(purchaseContract);

        // 创建订单
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setProduct(product);
        purchaseOrder.setPurchaseContract(purchaseContract);
        purchaseOrder.setQuantity(requestDto.getQuantity());
        purchaseOrder.setUnitPrice(requestDto.getUnitPrice());
        // 初始化总价或状态
        purchaseOrder.setTotalPrice(requestDto.getUnitPrice() * requestDto.getQuantity());
        purchaseOrder.setStatus(0);

        PurchaseOrder createdOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);
        return ResponseEntity.ok(createdOrder);
    }

    /**
     * 更新采购订单以及关联的采购合同
     * 参考 SalesOrderController 中的更新逻辑，统一从 JSON body 获取 contractContent
     */
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

        // 更新订单信息
        existingOrder.setSupplier(supplier);
        existingOrder.setProduct(product);
        existingOrder.setQuantity(requestDto.getQuantity());
        existingOrder.setUnitPrice(requestDto.getUnitPrice());
        existingOrder.setTotalPrice(requestDto.getUnitPrice() * requestDto.getQuantity());

        // 更新/创建采购合同
        PurchaseContract existingContract = existingOrder.getPurchaseContract();
        if (existingContract == null) {
            existingContract = new PurchaseContract();
            existingContract.setSupplier(supplier);
        }
        existingContract.setContractContent(requestDto.getContractContent());
        // 如有签署时间、到期时间等，需要时也可赋值
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

    /**
     * 删除采购订单并同时删除关联的采购合同
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable int id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        if (purchaseOrder != null && purchaseOrder.getPurchaseContract() != null) {
            purchaseContractService.deletePurchaseContract(purchaseOrder.getPurchaseContract().getContractId());
        }
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.noContent().build();
    }
}
package com.jason.supplymanagement.controller.AI;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-02-27
 * @de@description :
 */

import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Product.ProductCategory;
import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import com.jason.supplymanagement.service.Product.ProductCategoryService;
import com.jason.supplymanagement.service.Product.ProductService;
import com.jason.supplymanagement.service.Supply.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
public class AISearchController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SalesOrderService salesOrderService;

    /**
     * 1. AI搜索产品接口
     *
     * @param query    搜索关键词，对应产品名称
     * @param category_name 产品类别名称（需映射为 category_id）
     * @param page     页码（可选，默认1）
     * @param size     每页数量（可选，默认10）
     * @return 返回匹配的产品列表
     */
    @GetMapping("/product/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category_name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 初始化返回结果
        Map<String, Object> response = new HashMap<>();

        // 将类别名称映射为 category_id
        Integer categoryId = null;
        if (category_name != null) {
            categoryId = mapCategoryNameToId(category_name);
            if (categoryId == null) {
                response.put("status", "error");
                response.put("message", "类别名称无效");
                return ResponseEntity.badRequest().body(response);
            }
        }

        try {
            // 调用已有的产品搜索方法
            Page<Product> products = productService.getProducts(query, categoryId, PageRequest.of(page - 1, size));

            // 清洗结果：提取 name, description, category.categoryName, price
            List<Map<String, Object>> cleanedProducts = products.getContent().stream()
                    .map(product -> {
                        Map<String, Object> productMap = new HashMap<>();
                        productMap.put("name", product.getName());
                        productMap.put("description", product.getDescription());
                        if (product.getCategory() != null) {
                            productMap.put("category", product.getCategory().getCategoryName());
                        } else {
                            productMap.put("category", null); // 如果 category 为 null，设置为 null
                        }
                        productMap.put("price", product.getPrice());
                        return productMap;
                    })
                    .collect(Collectors.toList());

            // 构造返回结果
            response.put("status", "success");
            response.put("data", cleanedProducts);
            response.put("total", products.getTotalElements());
            response.put("page", page);
            response.put("size", size);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 根据类别名称映射为 category_id
     *
     * @param categoryName 类别名称
     * @return 对应的category_id，如果未找到则返回null
     */
    private Integer mapCategoryNameToId(String categoryName) {
        // 获取所有类别
        List<ProductCategory> categories = productCategoryService.getAllProductCategories();

        // 遍历查找匹配的类别
        for (ProductCategory category : categories) {
            if (categoryName.equalsIgnoreCase(category.getCategoryName())) {
                return category.getCategoryId();
            }
        }

        return null;
    }


    /**
     * 2.库存告警查询
     *
     * @return 返回告警状态的库存信息
     */
    @GetMapping("/inventory/alert")
    public ResponseEntity<Map<String, Object>> getInventoryAlerts() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取库存告警产品
            List<Product> lowStockProducts = productService.getLowStockProducts();

            // 清洗结果：只返回 name 和 category.categoryName
            List<Map<String, String>> cleanedProducts = lowStockProducts.stream()
                    .map(product -> {
                        Map<String, String> productMap = new HashMap<>();
                        productMap.put("name", product.getName());
                        if (product.getCategory() != null) {
                            productMap.put("categoryName", product.getCategory().getCategoryName());
                        } else {
                            productMap.put("categoryName", null); // 如果 category 为 null，设置为 null
                        }
                        return productMap;
                    })
                    .collect(Collectors.toList());

            response.put("status", "success");
            response.put("data", cleanedProducts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    /**
     * 3.查询进行中的物流订单
     *
     * @return 返回所有物流订单
     */
    @GetMapping("/orders/logistics")
    public ResponseEntity<Map<String, Object>> getAllLogisticsOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<LogisticsOrder> logisticsOrders = logisticsOrderService.getAllLogisticsOrders();
            response.put("status", "success");
            response.put("data", logisticsOrders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 4.查询采购或供应订单
     *
     * @return 返回所有采购（供应）订单
     */
    @GetMapping("/orders/supply")
    public ResponseEntity<Map<String, Object>> getAllPurchaseOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
            response.put("status", "success");
            response.put("data", purchaseOrders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 5.查询销售订单
     *
     * @return 返回所有销售订单
     */
    @GetMapping("/orders/sale")
    public ResponseEntity<Map<String, Object>> getAllSalesOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();
            response.put("status", "success");
            response.put("data", salesOrders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
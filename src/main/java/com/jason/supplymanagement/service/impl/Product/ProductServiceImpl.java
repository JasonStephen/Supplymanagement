package com.jason.supplymanagement.service.impl.Product;

import com.jason.supplymanagement.dao.Product.InventoryDAO;
import com.jason.supplymanagement.dao.Product.ProductCategoryDAO;
import com.jason.supplymanagement.dao.Product.ProductDAO;
import com.jason.supplymanagement.dto.ProductDetailsDTO;
import com.jason.supplymanagement.entity.Product.Inventory;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Product.ProductCategory;
import com.jason.supplymanagement.service.Product.InventoryService;
import com.jason.supplymanagement.service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private InventoryDAO inventoryDAO;

    @Autowired
    private ProductCategoryDAO productCategoryDAO;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productDAO.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Product getProductById(int id) {
        return productDAO.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        Product createdProduct = productDAO.save(product);
        Inventory inventory = new Inventory();
        inventory.setProductId(createdProduct.getProductId());
        inventory.setQuantity(0);
        inventory.setAlertThreshold(0);
        inventoryDAO.save(inventory);
        return createdProduct;
    }

    @Override
    public Product updateProduct(int id, Product product) {
        if (productDAO.existsById(id)) {
            product.setProductId(id);
            // 如果类别为 null，表示无类别
            if (product.getCategory() == null) {
                product.setCategory(null);
            }
            return productDAO.save(product);
        }
        return null;
    }


    @Override
    @Transactional
    public void deleteProduct(int id) {
        // 先删除相关的库存记录
        inventoryService.deleteByProductId(id);

        // 再删除产品
        productDAO.deleteById(id);
    }

    @Override
    public Page<Product> getProducts(String search, Integer category, Pageable pageable) {
        if (search != null && category != null) {
            return productDAO.findByNameContainingIgnoreCaseAndCategory_CategoryId(search, category, pageable);
        } else if (search != null) {
            return productDAO.findByNameContainingIgnoreCase(search, pageable);
        } else if (category != null) {
            return productDAO.findByCategory_CategoryId(category, pageable);
        } else {
            return productDAO.findAll(pageable);
        }
    }

    @Override
    public ResponseEntity<ProductDetailsDTO> getProductDetails(int productId) {
        // 获取产品信息
        Product product = productDAO.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build(); // 产品不存在时返回 404
        }

        // 获取库存信息
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        if (inventory == null) {
            inventory = new Inventory(); // 如果库存不存在，返回默认值
            inventory.setQuantity(0);
            inventory.setAlertThreshold(0);
        }

        // 构造 ProductDetailsDTO
        ProductDetailsDTO details = new ProductDetailsDTO();
        details.setProductId(product.getProductId());
        details.setName(product.getName());
        details.setCategory(product.getCategory().getCategoryName());
        details.setPrice(product.getPrice());
        details.setUnit(product.getUnit());
        details.setDescription(product.getDescription());
        details.setPhoto(product.getPhoto());
        details.setQuantity(inventory.getQuantity());
        details.setAlertThreshold(inventory.getAlertThreshold());
        details.setInventoryStatus(inventory.getQuantity() < inventory.getAlertThreshold() ? "告警" : "正常");

        return ResponseEntity.ok(details); // 返回包含 ProductDetailsDTO 的响应
    }

    @Override
    public boolean bindCategoryToProduct(int productId, int categoryId) {
        // 获取产品和类别
        Product product = productDAO.findById(productId).orElse(null);
        ProductCategory category = productCategoryDAO.findById(categoryId).orElse(null);

        if (product == null || category == null) {
            return false; // 产品或类别不存在
        }

        // 绑定类别到产品
        product.setCategory(category);
        productDAO.save(product);
        return true;
    }



}

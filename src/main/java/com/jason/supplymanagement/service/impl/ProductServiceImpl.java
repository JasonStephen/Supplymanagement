package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.InventoryDAO;
import com.jason.supplymanagement.dao.ProductDAO;
import com.jason.supplymanagement.entity.Inventory;
import com.jason.supplymanagement.entity.Product;
import com.jason.supplymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private InventoryDAO inventoryDAO;

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
            return productDAO.save(product);
        }
        return null;
    }

    @Override
    public void deleteProduct(int id) {
        productDAO.deleteById(id);
    }
}
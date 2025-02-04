package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.ProductComponentDAO;
import com.jason.supplymanagement.dao.ProductDAO;
import com.jason.supplymanagement.entity.Product;
import com.jason.supplymanagement.entity.ProductComponent;
import com.jason.supplymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductComponentDAO productComponentDAO;

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public Product getProductById(int id) {
        return productDAO.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        return productDAO.save(product);
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
        // Check if the product is used as a component in another product
        List<ProductComponent> components = productComponentDAO.findByComponent_ProductId(id);
        if (!components.isEmpty()) {
            throw new IllegalStateException("Cannot delete product as it is used as a component in another product.");
        }
        productDAO.deleteById(id);
    }
}
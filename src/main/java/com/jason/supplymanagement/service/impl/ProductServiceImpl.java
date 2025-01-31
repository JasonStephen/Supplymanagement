package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.ProductDAO;
import com.jason.supplymanagement.entity.Product;
import com.jason.supplymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

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
        productDAO.deleteById(id);
    }
}
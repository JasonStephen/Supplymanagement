package com.jason.supplymanagement.service.impl.Product;

import com.jason.supplymanagement.dao.Product.InventoryDAO;
import com.jason.supplymanagement.dao.Product.ProductDAO;
import com.jason.supplymanagement.entity.Product.Inventory;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

}

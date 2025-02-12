package com.jason.supplymanagement.service.impl.Product;

import com.jason.supplymanagement.dao.Product.ProductComponentDAO;
import com.jason.supplymanagement.entity.Product.ProductComponent;
import com.jason.supplymanagement.entity.Product.ProductComponentId;
import com.jason.supplymanagement.service.Product.ProductComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductComponentServiceImpl implements ProductComponentService {

    @Autowired
    private ProductComponentDAO productComponentDAO;

    @Override
    public List<ProductComponent> getAllProductComponents() {
        return productComponentDAO.findAll();
    }

    @Override
    public List<ProductComponent> getProductComponentsByProductId(int productId) {
        return productComponentDAO.findByProduct_ProductId(productId);
    }

    @Override
    public List<ProductComponent> getProductComponentsByComponentId(int componentId) {
        return productComponentDAO.findByComponent_ProductId(componentId);
    }

//    @Override
//    public ProductComponent createProductComponent(ProductComponent productComponent) {
//        return productComponentDAO.save(productComponent);
//    }

    @Override
    public ProductComponent createProductComponent(ProductComponent productComponent) {
        // 确保 product 和 component 的 ID 不为空
        if (productComponent.getProduct() == null || productComponent.getComponent() == null) {
            throw new IllegalArgumentException("Product and component must not be null");
        }
        return productComponentDAO.save(productComponent);
    }

    @Override
    public ProductComponent updateProductComponent(ProductComponent productComponent) {
        ProductComponentId id = new ProductComponentId(productComponent.getProduct().getProductId(), productComponent.getComponent().getProductId());
        if (productComponentDAO.existsById(id)) {
            return productComponentDAO.save(productComponent);
        }
        return null;
    }

    @Override
    public void deleteProductComponent(ProductComponent productComponent) {
        productComponentDAO.delete(productComponent);
    }

    @Override
    public ProductComponent getProductComponentById(ProductComponentId id) {
        return productComponentDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteProductComponentById(ProductComponentId id) {
        productComponentDAO.deleteById(id);
    }
}
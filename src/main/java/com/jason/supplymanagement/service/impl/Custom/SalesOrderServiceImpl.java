package com.jason.supplymanagement.service.impl.Custom;

import com.jason.supplymanagement.dao.Custom.SalesOrderDAO;
import com.jason.supplymanagement.entity.Custom.SalesOrder;
import com.jason.supplymanagement.service.Custom.CustomerService;
import com.jason.supplymanagement.service.Custom.SalesOrderService;
import com.jason.supplymanagement.service.Custom.SalesContractService;
import com.jason.supplymanagement.service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    private SalesOrderDAO salesOrderDAO;

    @Autowired
    private SalesContractService salesContractService;

    @Autowired
    private ProductService productService;

    @Override
    public List<SalesOrder> getAllSalesOrders() {
        List<SalesOrder> salesOrders = salesOrderDAO.findAll();
        for (SalesOrder order : salesOrders) {
            order.setContract(salesContractService.getSalesContractById(order.getContractId()));
        }
        return salesOrders;
    }

    @Override
    public SalesOrder getSalesOrderById(int id) {
        SalesOrder salesOrder = salesOrderDAO.findById(id).orElse(null);
        if (salesOrder != null) {
            salesOrder.setContract(salesContractService.getSalesContractById(salesOrder.getContractId()));
        }
        return salesOrder;
    }

    @Override
    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        return salesOrderDAO.save(salesOrder);
    }

    @Override
    public SalesOrder updateSalesOrder(int id, SalesOrder salesOrder) {
        if (salesOrderDAO.existsById(id)) {
            salesOrder.setSalesOrderId(id);
            return salesOrderDAO.save(salesOrder);
        }
        return null;
    }

    @Override
    public void deleteSalesOrder(int id) {
        salesOrderDAO.deleteById(id);
    }

//    @Override
//    public List<SalesOrder> getNewSalesOrders(int limit) {
//        return salesOrderDAO.findAll(Sort.by(Sort.Direction.DESC, "salesOrderId")).stream()
//                .filter(order -> order.getStatus() == 0)
//                .limit(limit)
//                .map(order -> {
//                    // 填充 product 字段
//                    order.setProduct(productService.getProductById(order.getProductId()));
//                    return order;
//                })
//                .collect(Collectors.toList());
//    }
//
    @Override
    public List<SalesOrder> getNewSalesOrders(int limit) {
        List<SalesOrder> orders = salesOrderDAO.findAll(Sort.by(Sort.Direction.DESC, "salesOrderId")).stream()
                .filter(order -> "0".equals(order.getStatus())) // 只返回 status="0" 的订单
                .limit(limit)
                .collect(Collectors.toList());
        return orders != null ? orders : new ArrayList<>();
    }

}
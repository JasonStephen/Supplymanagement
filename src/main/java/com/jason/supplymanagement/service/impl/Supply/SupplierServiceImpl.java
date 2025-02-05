package com.jason.supplymanagement.service.impl.Supply;

import com.jason.supplymanagement.dao.Supply.SupplierDAO;
import com.jason.supplymanagement.entity.Supply.Supplier;
import com.jason.supplymanagement.service.Supply.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierDAO supplierDAO;

    @Override
    public Supplier saveSupplier(Supplier supplier) {
        return supplierDAO.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) {
        return supplierDAO.save(supplier);
    }

    @Override
    public void deleteSupplier(int supplierId) {
        supplierDAO.deleteById(supplierId);
    }

    @Override
    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.findById(supplierId).orElse(null);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.findAll();
    }
}
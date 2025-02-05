package com.jason.supplymanagement.service.Supply;

import com.jason.supplymanagement.entity.Supply.Supplier;
import java.util.List;

public interface SupplierService {
    Supplier saveSupplier(Supplier supplier);
    Supplier updateSupplier(Supplier supplier);
    void deleteSupplier(int supplierId);
    Supplier getSupplierById(int supplierId);
    List<Supplier> getAllSuppliers();
}
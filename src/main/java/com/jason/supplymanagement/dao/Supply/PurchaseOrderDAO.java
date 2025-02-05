package com.jason.supplymanagement.dao.Supply;

import com.jason.supplymanagement.entity.Supply.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDAO extends JpaRepository<PurchaseOrder, Integer> {
    List<PurchaseOrder> findBySupplier_SupplierId(int supplierId);
}
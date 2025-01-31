package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.PurchaseContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseContractDAO extends JpaRepository<PurchaseContract, Integer> {
    // 修改方法名，使用 supplier_SupplierId 作为路径
    List<PurchaseContract> findBySupplier_SupplierId(int supplierId);
}

package com.jason.supplymanagement.dao.Supply;

import com.jason.supplymanagement.entity.Supply.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDAO extends JpaRepository<Supplier, Integer> {
    // 自定义方法：根据供应商名称查询供应商
    Supplier findByName(String name);
}
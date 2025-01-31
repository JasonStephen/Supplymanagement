package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialDAO extends JpaRepository<Material, Integer> {
    // 自定义方法：根据原材料名称查询原材料
    Material findByName(String name);
}

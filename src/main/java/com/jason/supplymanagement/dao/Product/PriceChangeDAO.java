package com.jason.supplymanagement.dao.Product;

import com.jason.supplymanagement.entity.Product.PriceChange;
import com.jason.supplymanagement.entity.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-04-10 16:43:23
 * Github: https://github.com/JasonStephen
 */

public interface PriceChangeDAO extends JpaRepository<PriceChange, Integer> {
    List<PriceChange> findByProductOrderByChangeDateDesc(Product product);
}

package com.jason.supplymanagement.service.Custom;

import com.jason.supplymanagement.entity.Custom.SalesContract;

import java.util.List;

public interface SalesContractService {
    List<SalesContract> getAllSalesContracts();
    SalesContract getSalesContractById(int id);
    SalesContract createSalesContract(SalesContract salesContract);
    SalesContract updateSalesContract(int id, SalesContract salesContract);
    void deleteSalesContract(int id);
}
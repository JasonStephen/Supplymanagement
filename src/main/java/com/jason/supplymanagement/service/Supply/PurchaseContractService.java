package com.jason.supplymanagement.service.Supply;

import com.jason.supplymanagement.entity.Supply.PurchaseContract;

import java.util.List;

public interface PurchaseContractService {
    List<PurchaseContract> getAllPurchaseContracts();
    PurchaseContract getPurchaseContractById(int id);
    PurchaseContract createPurchaseContract(PurchaseContract purchaseContract);
    PurchaseContract updatePurchaseContract(int id, PurchaseContract purchaseContract);
    void deletePurchaseContract(int id);
}
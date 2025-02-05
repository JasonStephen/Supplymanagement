package com.jason.supplymanagement.service.impl.Supply;

import com.jason.supplymanagement.dao.Supply.PurchaseContractDAO;
import com.jason.supplymanagement.entity.Supply.PurchaseContract;
import com.jason.supplymanagement.service.Supply.PurchaseContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseContractServiceImpl implements PurchaseContractService {

    @Autowired
    private PurchaseContractDAO purchaseContractDAO;

    @Override
    public List<PurchaseContract> getAllPurchaseContracts() {
        return purchaseContractDAO.findAll();
    }

    @Override
    public PurchaseContract getPurchaseContractById(int id) {
        return purchaseContractDAO.findById(id).orElse(null);
    }

    @Override
    public PurchaseContract createPurchaseContract(PurchaseContract purchaseContract) {
        return purchaseContractDAO.save(purchaseContract);
    }

    @Override
    public PurchaseContract updatePurchaseContract(int id, PurchaseContract purchaseContract) {
        if (purchaseContractDAO.existsById(id)) {
            purchaseContract.setContractId(id);
            return purchaseContractDAO.save(purchaseContract);
        }
        return null;
    }

    @Override
    public void deletePurchaseContract(int id) {
        purchaseContractDAO.deleteById(id);
    }
}
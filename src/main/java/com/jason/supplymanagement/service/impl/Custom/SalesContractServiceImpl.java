package com.jason.supplymanagement.service.impl.Custom;

import com.jason.supplymanagement.dao.Custom.SalesContractDAO;
import com.jason.supplymanagement.entity.Custom.SalesContract;
import com.jason.supplymanagement.service.Custom.SalesContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesContractServiceImpl implements SalesContractService {

    @Autowired
    private SalesContractDAO salesContractDAO;

    @Override
    public List<SalesContract> getAllSalesContracts() {
        return salesContractDAO.findAll();
    }

    @Override
    public SalesContract getSalesContractById(int id) {
        return salesContractDAO.findById(id).orElse(null);
    }

    @Override
    public SalesContract createSalesContract(SalesContract salesContract) {
        return salesContractDAO.save(salesContract);
    }

    @Override
    public SalesContract updateSalesContract(int id, SalesContract salesContract) {
        if (salesContractDAO.existsById(id)) {
            salesContract.setContractId(id);
            return salesContractDAO.save(salesContract);
        }
        return null;
    }

    @Override
    public void deleteSalesContract(int id) {
        salesContractDAO.deleteById(id);
    }
}
package com.jason.supplymanagement.controller.Custom;

import com.jason.supplymanagement.entity.Custom.SalesContract;
import com.jason.supplymanagement.service.Custom.SalesContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-contracts")
public class SalesContractController {

    @Autowired
    private SalesContractService salesContractService;

    @GetMapping
    public List<SalesContract> getAllSalesContracts() {
        return salesContractService.getAllSalesContracts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesContract> getSalesContractById(@PathVariable int id) {
        SalesContract salesContract = salesContractService.getSalesContractById(id);
        if (salesContract != null) {
            return ResponseEntity.ok(salesContract);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public SalesContract createSalesContract(@RequestBody SalesContract salesContract) {
        return salesContractService.createSalesContract(salesContract);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesContract> updateSalesContract(@PathVariable int id, @RequestBody SalesContract salesContract) {
        SalesContract updatedSalesContract = salesContractService.updateSalesContract(id, salesContract);
        if (updatedSalesContract != null) {
            return ResponseEntity.ok(updatedSalesContract);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesContract(@PathVariable int id) {
        salesContractService.deleteSalesContract(id);
        return ResponseEntity.noContent().build();
    }
}
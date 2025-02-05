package com.jason.supplymanagement.controller.Supply;

import com.jason.supplymanagement.entity.Supply.PurchaseContract;
import com.jason.supplymanagement.service.Supply.PurchaseContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-contracts")
public class PurchaseContractController {

    @Autowired
    private PurchaseContractService purchaseContractService;

    @GetMapping
    public List<PurchaseContract> getAllPurchaseContracts() {
        return purchaseContractService.getAllPurchaseContracts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseContract> getPurchaseContractById(@PathVariable int id) {
        PurchaseContract purchaseContract = purchaseContractService.getPurchaseContractById(id);
        if (purchaseContract != null) {
            return ResponseEntity.ok(purchaseContract);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public PurchaseContract createPurchaseContract(@RequestBody PurchaseContract purchaseContract) {
        return purchaseContractService.createPurchaseContract(purchaseContract);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseContract> updatePurchaseContract(@PathVariable int id, @RequestBody PurchaseContract purchaseContract) {
        PurchaseContract updatedPurchaseContract = purchaseContractService.updatePurchaseContract(id, purchaseContract);
        if (updatedPurchaseContract != null) {
            return ResponseEntity.ok(updatedPurchaseContract);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseContract(@PathVariable int id) {
        purchaseContractService.deletePurchaseContract(id);
        return ResponseEntity.noContent().build();
    }
}
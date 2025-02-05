package com.jason.supplymanagement.controller.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logistics-orders")
public class LogisticsOrderController {

    @Autowired
    private LogisticsOrderService logisticsOrderService;

    @GetMapping
    public List<LogisticsOrder> getAllLogisticsOrders() {
        return logisticsOrderService.getAllLogisticsOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogisticsOrder> getLogisticsOrderById(@PathVariable int id) {
        LogisticsOrder logisticsOrder = logisticsOrderService.getLogisticsOrderById(id);
        if (logisticsOrder != null) {
            return ResponseEntity.ok(logisticsOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public LogisticsOrder createLogisticsOrder(@RequestBody LogisticsOrder logisticsOrder) {
        return logisticsOrderService.createLogisticsOrder(logisticsOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogisticsOrder> updateLogisticsOrder(@PathVariable int id, @RequestBody LogisticsOrder logisticsOrder) {
        LogisticsOrder updatedLogisticsOrder = logisticsOrderService.updateLogisticsOrder(id, logisticsOrder);
        if (updatedLogisticsOrder != null) {
            return ResponseEntity.ok(updatedLogisticsOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogisticsOrder(@PathVariable int id) {
        logisticsOrderService.deleteLogisticsOrder(id);
        return ResponseEntity.noContent().build();
    }
}
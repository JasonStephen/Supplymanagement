package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.Material;
import com.jason.supplymanagement.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable int id) {
        return materialService.getMaterialById(id);
    }

    @PostMapping
    public Material createMaterial(@RequestBody Material material) {
        return materialService.createMaterial(material);
    }

    @PutMapping("/{id}")
    public Material updateMaterial(@PathVariable int id, @RequestBody Material material) {
        return materialService.updateMaterial(id, material);
    }

    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable int id) {
        materialService.deleteMaterial(id);
    }
}
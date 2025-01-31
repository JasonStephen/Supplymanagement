package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.Material;

import java.util.List;

public interface MaterialService {
    List<Material> getAllMaterials();
    Material getMaterialById(int id);
    Material createMaterial(Material material);
    Material updateMaterial(int id, Material material);
    void deleteMaterial(int id);
}
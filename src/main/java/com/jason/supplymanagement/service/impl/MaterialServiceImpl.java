package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.MaterialDAO;
import com.jason.supplymanagement.entity.Material;
import com.jason.supplymanagement.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialDAO materialDAO;

    @Override
    public List<Material> getAllMaterials() {
        return materialDAO.findAll();
    }

    @Override
    public Material getMaterialById(int id) {
        return materialDAO.findById(id).orElse(null);
    }

    @Override
    public Material createMaterial(Material material) {
        return materialDAO.save(material);
    }

    @Override
    public Material updateMaterial(int id, Material material) {
        if (materialDAO.existsById(id)) {
            material.setMaterialId(id);
            return materialDAO.save(material);
        }
        return null;
    }

    @Override
    public void deleteMaterial(int id) {
        materialDAO.deleteById(id);
    }
}
package com.sports.equipment.service;

import com.sports.equipment.model.Equipment;
import com.sports.equipment.util.DataPersistence;
import com.sports.equipment.util.IDGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EquipmentService handles equipment inventory management.
 */
public class EquipmentService {
    private List<Equipment> equipment;
    private DataPersistence persistence;

    public EquipmentService(DataPersistence persistence) {
        this.persistence = persistence;
        this.equipment = persistence.loadEquipment();
    }

    /**
     * Add new equipment to inventory
     */
    public boolean addEquipment(String name, String category, String description, int quantity) {
        Equipment newEquip = new Equipment(
                IDGenerator.generateEquipmentId(),
                name,
                category,
                description,
                quantity,
                IDGenerator.getCurrentDate()
        );

        equipment.add(newEquip);
        persistence.saveEquipment(equipment);
        return true;
    }

    /**
     * Get equipment by ID
     */
    public Optional<Equipment> getEquipmentById(String equipmentId) {
        return equipment.stream().filter(e -> e.getId().equals(equipmentId)).findFirst();
    }

    /**
     * Get all equipment
     */
    public List<Equipment> getAllEquipment() {
        return new java.util.ArrayList<>(equipment);
    }

    /**
     * Get available equipment
     */
    public List<Equipment> getAvailableEquipment() {
        return equipment.stream()
                .filter(e -> e.getAvailableQuantity() > 0 && 
                            e.getStatus() == Equipment.EquipmentStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Search equipment by name
     */
    public List<Equipment> searchEquipment(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return equipment.stream()
                .filter(e -> e.getName().toLowerCase().contains(lowerKeyword) ||
                            e.getCategory().toLowerCase().contains(lowerKeyword) ||
                            e.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Filter equipment by category
     */
    public List<Equipment> filterByCategory(String category) {
        return equipment.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filter equipment by availability
     */
    public List<Equipment> filterByAvailability(boolean available) {
        return equipment.stream()
                .filter(e -> (available && e.getAvailableQuantity() > 0) ||
                            (!available && e.getAvailableQuantity() == 0))
                .collect(Collectors.toList());
    }

    /**
     * Update equipment quantity
     */
    public void updateEquipmentQuantity(String equipmentId, int newQuantity) {
        Optional<Equipment> equip = getEquipmentById(equipmentId);
        if (equip.isPresent()) {
            Equipment e = equip.get();
            e.setQuantity(newQuantity);
            e.setLastModifiedDate(IDGenerator.getCurrentDate());
            persistence.saveEquipment(equipment);
        }
    }

    /**
     * Update equipment available quantity
     */
    public void updateAvailableQuantity(String equipmentId, int change) {
        Optional<Equipment> equip = getEquipmentById(equipmentId);
        if (equip.isPresent()) {
            Equipment e = equip.get();
            e.setAvailableQuantity(Math.max(0, e.getAvailableQuantity() + change));
            e.setLastModifiedDate(IDGenerator.getCurrentDate());
            persistence.saveEquipment(equipment);
        }
    }

    /**
     * Update equipment status
     */
    public void updateEquipmentStatus(String equipmentId, Equipment.EquipmentStatus status) {
        Optional<Equipment> equip = getEquipmentById(equipmentId);
        if (equip.isPresent()) {
            Equipment e = equip.get();
            e.setStatus(status);
            e.setLastModifiedDate(IDGenerator.getCurrentDate());
            persistence.saveEquipment(equipment);
        }
    }

    /**
     * Delete equipment
     */
    public boolean deleteEquipment(String equipmentId) {
        boolean removed = equipment.removeIf(e -> e.getId().equals(equipmentId));
        if (removed) {
            persistence.saveEquipment(equipment);
        }
        return removed;
    }

    /**
     * Get all unique categories
     */
    public List<String> getAllCategories() {
        return equipment.stream()
                .map(Equipment::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Update equipment details
     */
    public void updateEquipment(Equipment equip) {
        equip.setLastModifiedDate(IDGenerator.getCurrentDate());
        equipment.replaceAll(e -> e.getId().equals(equip.getId()) ? equip : e);
        persistence.saveEquipment(equipment);
    }
}

package com.sports.equipment.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * MaintenanceRecord model for tracking equipment maintenance.
 */
public class MaintenanceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String equipmentId;
    private String date;
    private MaintenanceType type;
    private String description;
    private String recordedById;
    private String completionDate;

    public enum MaintenanceType {
        REPAIR, MAINTENANCE, INSPECTION, CLEANING
    }

    public MaintenanceRecord() {
    }

    public MaintenanceRecord(String id, String equipmentId, String date, 
                            MaintenanceType type, String description, String recordedById) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.date = date;
        this.type = type;
        this.description = description;
        this.recordedById = recordedById;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MaintenanceType getType() {
        return type;
    }

    public void setType(MaintenanceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordedById() {
        return recordedById;
    }

    public void setRecordedById(String recordedById) {
        this.recordedById = recordedById;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceRecord that = (MaintenanceRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "id='" + id + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", type=" + type +
                '}';
    }
}

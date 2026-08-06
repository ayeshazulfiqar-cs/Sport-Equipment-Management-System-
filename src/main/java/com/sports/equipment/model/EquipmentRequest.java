package com.sports.equipment.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * EquipmentRequest model representing a request for equipment.
 */
public class EquipmentRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String equipmentId;
    private int quantity;
    private String requestDate;
    private String approvalDate;
    private RequestStatus status;
    private String rejectionReason;
    private String approvedById;

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, ISSUED, RETURNED
    }

    public EquipmentRequest() {
        this.status = RequestStatus.PENDING;
    }

    public EquipmentRequest(String id, String userId, String equipmentId, 
                           int quantity, String requestDate) {
        this();
        this.id = id;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.requestDate = requestDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(String approvedById) {
        this.approvedById = approvedById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentRequest that = (EquipmentRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EquipmentRequest{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}

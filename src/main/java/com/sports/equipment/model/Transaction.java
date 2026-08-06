package com.sports.equipment.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Transaction model representing issue and return of equipment.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String requestId;
    private String userId;
    private String equipmentId;
    private int quantity;
    private String issueDate;
    private String dueDate;
    private String returnDate;
    private TransactionStatus status;
    private double penalty;
    private String issuedById;

    public enum TransactionStatus {
        ISSUED, RETURNED, OVERDUE
    }

    public Transaction() {
        this.penalty = 0.0;
    }

    public Transaction(String id, String requestId, String userId, String equipmentId,
                      int quantity, String issueDate, String dueDate, String issuedById) {
        this();
        this.id = id;
        this.requestId = requestId;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = TransactionStatus.ISSUED;
        this.issuedById = issuedById;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public String getIssuedById() {
        return issuedById;
    }

    public void setIssuedById(String issuedById) {
        this.issuedById = issuedById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", status=" + status +
                '}';
    }
}

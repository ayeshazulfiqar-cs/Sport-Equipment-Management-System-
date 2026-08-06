package com.sports.equipment.service;

import com.sports.equipment.model.EquipmentRequest;
import com.sports.equipment.util.DataPersistence;
import com.sports.equipment.util.IDGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RequestService handles equipment requests workflow.
 */
public class RequestService {
    private List<EquipmentRequest> requests;
    private DataPersistence persistence;

    public RequestService(DataPersistence persistence) {
        this.persistence = persistence;
        this.requests = persistence.loadRequests();
    }

    /**
     * Create a new equipment request
     */
    public String createRequest(String userId, String equipmentId, int quantity) {
        EquipmentRequest request = new EquipmentRequest(
                IDGenerator.generateRequestId(),
                userId,
                equipmentId,
                quantity,
                IDGenerator.getCurrentDate()
        );

        requests.add(request);
        persistence.saveRequests(requests);
        return request.getId();
    }

    /**
     * Get request by ID
     */
    public Optional<EquipmentRequest> getRequestById(String requestId) {
        return requests.stream().filter(r -> r.getId().equals(requestId)).findFirst();
    }

    /**
     * Get all requests
     */
    public List<EquipmentRequest> getAllRequests() {
        return new java.util.ArrayList<>(requests);
    }

    /**
     * Get pending requests
     */
    public List<EquipmentRequest> getPendingRequests() {
        return requests.stream()
                .filter(r -> r.getStatus() == EquipmentRequest.RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Get requests for a specific user
     */
    public List<EquipmentRequest> getUserRequests(String userId) {
        return requests.stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Get requests for a specific equipment
     */
    public List<EquipmentRequest> getEquipmentRequests(String equipmentId) {
        return requests.stream()
                .filter(r -> r.getEquipmentId().equals(equipmentId))
                .collect(Collectors.toList());
    }

    /**
     * Approve a request
     */
    public void approveRequest(String requestId, String approvedById) {
        Optional<EquipmentRequest> request = getRequestById(requestId);
        if (request.isPresent()) {
            EquipmentRequest r = request.get();
            r.setStatus(EquipmentRequest.RequestStatus.APPROVED);
            r.setApprovalDate(IDGenerator.getCurrentDate());
            r.setApprovedById(approvedById);
            persistence.saveRequests(requests);
        }
    }

    /**
     * Reject a request
     */
    public void rejectRequest(String requestId, String rejectionReason, String approvedById) {
        Optional<EquipmentRequest> request = getRequestById(requestId);
        if (request.isPresent()) {
            EquipmentRequest r = request.get();
            r.setStatus(EquipmentRequest.RequestStatus.REJECTED);
            r.setApprovalDate(IDGenerator.getCurrentDate());
            r.setRejectionReason(rejectionReason);
            r.setApprovedById(approvedById);
            persistence.saveRequests(requests);
        }
    }

    /**
     * Update request status to issued
     */
    public void markAsIssued(String requestId) {
        Optional<EquipmentRequest> request = getRequestById(requestId);
        if (request.isPresent()) {
            EquipmentRequest r = request.get();
            r.setStatus(EquipmentRequest.RequestStatus.ISSUED);
            persistence.saveRequests(requests);
        }
    }

    /**
     * Update request status to returned
     */
    public void markAsReturned(String requestId) {
        Optional<EquipmentRequest> request = getRequestById(requestId);
        if (request.isPresent()) {
            EquipmentRequest r = request.get();
            r.setStatus(EquipmentRequest.RequestStatus.RETURNED);
            persistence.saveRequests(requests);
        }
    }

    /**
     * Update request
     */
    public void updateRequest(EquipmentRequest request) {
        requests.replaceAll(r -> r.getId().equals(request.getId()) ? request : r);
        persistence.saveRequests(requests);
    }

    /**
     * Get all approved but not issued requests
     */
    public List<EquipmentRequest> getApprovedRequests() {
        return requests.stream()
                .filter(r -> r.getStatus() == EquipmentRequest.RequestStatus.APPROVED)
                .collect(Collectors.toList());
    }
}

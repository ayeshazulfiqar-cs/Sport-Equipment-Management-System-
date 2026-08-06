package com.sports.equipment.service;

import com.sports.equipment.model.Transaction;
import com.sports.equipment.util.DataPersistence;
import com.sports.equipment.util.IDGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TransactionService handles issuance and return of equipment.
 */
public class TransactionService {
    private List<Transaction> transactions;
    private DataPersistence persistence;

    public TransactionService(DataPersistence persistence) {
        this.persistence = persistence;
        this.transactions = persistence.loadTransactions();
    }

    /**
     * Create a new transaction (issue equipment)
     */
    public String issueEquipment(String requestId, String userId, String equipmentId,
                                 int quantity, int durationDays, String issuedById) {
        Transaction transaction = new Transaction(
                IDGenerator.generateTransactionId(),
                requestId,
                userId,
                equipmentId,
                quantity,
                IDGenerator.getCurrentDate(),
                IDGenerator.getDateAfterDays(durationDays),
                issuedById
        );

        transactions.add(transaction);
        persistence.saveTransactions(transactions);
        return transaction.getId();
    }

    /**
     * Get transaction by ID
     */
    public Optional<Transaction> getTransactionById(String transactionId) {
        return transactions.stream().filter(t -> t.getId().equals(transactionId)).findFirst();
    }

    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() {
        return new java.util.ArrayList<>(transactions);
    }

    /**
     * Get user transactions
     */
    public List<Transaction> getUserTransactions(String userId) {
        return transactions.stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Get overdue transactions
     */
    public List<Transaction> getOverdueTransactions() {
        String today = IDGenerator.getCurrentDate();
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED &&
                            t.getDueDate().compareTo(today) < 0)
                .collect(Collectors.toList());
    }

    /**
     * Return equipment and calculate penalty
     */
    public double returnEquipment(String transactionId, boolean damaged) {
        Optional<Transaction> trans = getTransactionById(transactionId);
        if (trans.isPresent()) {
            Transaction t = trans.get();
            t.setReturnDate(IDGenerator.getCurrentDate());
            t.setStatus(Transaction.TransactionStatus.RETURNED);

            // Calculate penalty if late
            double penalty = 0.0;
            int daysLate = IDGenerator.getDaysBetween(t.getDueDate(), t.getReturnDate());
            if (daysLate > 0) {
                penalty = daysLate * 10.0; // 10 per day late
            }

            if (damaged) {
                penalty += 50.0; // Additional penalty for damage
            }

            t.setPenalty(penalty);
            persistence.saveTransactions(transactions);
            return penalty;
        }
        return 0.0;
    }

    /**
     * Get current active transactions (issued but not returned)
     */
    public List<Transaction> getActiveTransactions() {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                .collect(Collectors.toList());
    }

    /**
     * Update transaction
     */
    public void updateTransaction(Transaction transaction) {
        transactions.replaceAll(t -> t.getId().equals(transaction.getId()) ? transaction : t);
        persistence.saveTransactions(transactions);
    }

    /**
     * Get transaction by request ID
     */
    public Optional<Transaction> getTransactionByRequestId(String requestId) {
        return transactions.stream().filter(t -> t.getRequestId().equals(requestId)).findFirst();
    }

    /**
     * Calculate total penalties for a user
     */
    public double getTotalPenalties(String userId) {
        return transactions.stream()
                .filter(t -> t.getUserId().equals(userId))
                .mapToDouble(Transaction::getPenalty)
                .sum();
    }
}

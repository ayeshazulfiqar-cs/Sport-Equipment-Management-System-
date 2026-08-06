package com.sports.equipment.service;

import com.sports.equipment.model.User;
import com.sports.equipment.model.Transaction;
import com.sports.equipment.util.DataPersistence;
import com.sports.equipment.util.IDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private List<User> users = new ArrayList<>();
    private DataPersistence dataPersistence;

    public UserService(DataPersistence dataPersistence) {
        this.dataPersistence = dataPersistence;
        // Load existing users from file on startup
        List<User> loaded = dataPersistence.loadUsers();
        if (loaded != null) {
            users.addAll(loaded);
        }
    }

    // LOGIN — returns User or null
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // REGISTER — fixed constructor argument order
    public boolean register(String name, String email, String username,
                            String password, User.UserRole role) {

        // Check username not already taken
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        // Generate a unique ID for the new user
        String newId = IDGenerator.generateUserId(); // ✔ FIXED: correct method name

        // ✔ FIXED: correct order — id, name, email, username, password, role
        User newUser = new User(
                newId,      // id
                name,       // name
                email,      // email
                username,   // username
                password,   // password
                role        // role
        );

        users.add(newUser);

        // Save to file so the user persists after restart
        dataPersistence.saveUsers(users);

        return true;
    }

    public void logout() {
        System.out.println("User logged out");
    }

    // GET ALL USERS
    public List<User> getAllUsers() {
        return users;
    }

    // Get a single user by ID
    public User getUserById(String userId) {
        for (User u : users) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    // TRANSACTIONS
    public List<Transaction> getActiveTransactionsForUser(
            TransactionService transactionService, String userId) {
        return transactionService.getUserTransactions(userId)
                .stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                .collect(Collectors.toList());
    }

    public List<Transaction> getUpdatedTransactions(
            TransactionService transactionService, String userId) {
        return transactionService.getUserTransactions(userId)
                .stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                .collect(Collectors.toList());
    }
}
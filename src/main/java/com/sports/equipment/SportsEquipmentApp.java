package com.sports.equipment;

import com.sports.equipment.service.*;
import com.sports.equipment.ui.LoginWindow;
import com.sports.equipment.util.DataPersistence;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application entry point for Sports Equipment Management System.
 */
public class SportsEquipmentApp extends Application {
    private static DataPersistence persistence;
    private static UserService userService;
    private static EquipmentService equipmentService;
    private static RequestService requestService;
    private static TransactionService transactionService;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("APP STARTED");
        initializeServices();
        showLoginWindow(primaryStage);
    }

    private void initializeServices() {
        persistence = new DataPersistence();
        userService = new UserService(persistence); // ← FIXED: pass persistence
        equipmentService = new EquipmentService(persistence);
        requestService = new RequestService(persistence);
        transactionService = new TransactionService(persistence);
    }

    private void showLoginWindow(Stage primaryStage) {
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.show(primaryStage);
    }

    public static UserService getUserService() {
        return userService;
    }

    public static EquipmentService getEquipmentService() {
        return equipmentService;
    }

    public static RequestService getRequestService() {
        return requestService;
    }

    public static TransactionService getTransactionService() {
        return transactionService;
    }

    public static DataPersistence getPersistence() {
        return persistence;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
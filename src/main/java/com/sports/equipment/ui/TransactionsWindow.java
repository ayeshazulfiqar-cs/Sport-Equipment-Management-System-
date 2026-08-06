package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.Transaction;
import com.sports.equipment.model.User;
import com.sports.equipment.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Window for sports head to view transactions.
 */
public class TransactionsWindow {

    private TransactionService transactionService;

    public TransactionsWindow() {
        this.transactionService = SportsEquipmentApp.getTransactionService();
    }

    public void show(User user, BorderPane parentPane) {

        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("Equipment Transactions");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // ---------------- FILTER CONTROLS ----------------
        HBox filterBox = new HBox(10);

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(
                "All Statuses",
                "ISSUED",
                "RETURNED",
                "OVERDUE"
        );
        statusCombo.setValue("All Statuses");

        Button filterButton = new Button("Filter");

        filterBox.getChildren().addAll(
                new Label("Status:"),
                statusCombo,
                filterButton
        );

        // ---------------- TABLE ----------------
        TableView<Transaction> table = createTransactionsTable();
        Label statusLabel = new Label();

        // Load all transactions
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        table.getItems().addAll(allTransactions);
        statusLabel.setText("Total transactions: " + allTransactions.size());

        // ---------------- FILTER ACTION ----------------
        filterButton.setOnAction(e -> {

            String selectedStatus = statusCombo.getValue();
            List<Transaction> filtered = transactionService.getAllTransactions();

            if (selectedStatus.equals("OVERDUE")) {

                filtered = filtered.stream()
                        .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                        .filter(t -> t.getDueDate().compareTo(
                                com.sports.equipment.util.IDGenerator.getCurrentDate()) < 0)
                        .collect(Collectors.toList());

            } else if (!selectedStatus.equals("All Statuses")) {

                filtered = filtered.stream()
                        .filter(t -> t.getStatus().toString().equals(selectedStatus))
                        .collect(Collectors.toList());
            }

            table.getItems().clear();
            table.getItems().addAll(filtered);

            statusLabel.setText("Showing " + filtered.size() + " transaction(s)");
        });

        // ---------------- SUMMARY PANEL ----------------
        VBox summaryPanel = new VBox(10);
        summaryPanel.setPadding(new Insets(15));
        summaryPanel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");

        int activeCount = (int) allTransactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                .count();

        int returnedCount = (int) allTransactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.RETURNED)
                .count();

        int overdueCount = (int) allTransactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                .filter(t -> t.getDueDate().compareTo(
                        com.sports.equipment.util.IDGenerator.getCurrentDate()) < 0)
                .count();

        Label summaryLabel = new Label("Summary");
        summaryLabel.setStyle("-fx-font-weight: bold;");

        Label activeLbl = new Label("Active Transactions: " + activeCount);
        Label returnedLbl = new Label("Returned Transactions: " + returnedCount);
        Label overdueLbl = new Label("Overdue Transactions: " + overdueCount);
        overdueLbl.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        summaryPanel.getChildren().addAll(
                summaryLabel,
                activeLbl,
                returnedLbl,
                overdueLbl
        );

        // ---------------- FINAL LAYOUT ----------------
        contentArea.getChildren().addAll(
                titleLabel,
                filterBox,
                table,
                summaryPanel,
                statusLabel
        );

        parentPane.setCenter(contentArea);
    }

    // ---------------- TABLE ----------------
    private TableView<Transaction> createTransactionsTable() {

        TableView<Transaction> table = new TableView<>();

        TableColumn<Transaction, String> idCol = new TableColumn<>("Transaction ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getId())
        );

        TableColumn<Transaction, String> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getUserId())
        );

        TableColumn<Transaction, String> equipmentCol = new TableColumn<>("Equipment ID");
        equipmentCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEquipmentId())
        );

        TableColumn<Transaction, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getQuantity())
        );

        TableColumn<Transaction, String> issueCol = new TableColumn<>("Issue Date");
        issueCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getIssueDate())
        );

        TableColumn<Transaction, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDueDate())
        );

        TableColumn<Transaction, String> returnCol = new TableColumn<>("Return Date");
        returnCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getReturnDate() != null ? c.getValue().getReturnDate() : "-"
                )
        );

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus().toString())
        );

        TableColumn<Transaction, Double> penaltyCol = new TableColumn<>("Penalty");
        penaltyCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPenalty())
        );

        table.getColumns().addAll(
                idCol,
                userCol,
                equipmentCol,
                qtyCol,
                issueCol,
                dueCol,
                returnCol,
                statusCol,
                penaltyCol
        );

        table.setPrefHeight(400);
        return table;
    }
}
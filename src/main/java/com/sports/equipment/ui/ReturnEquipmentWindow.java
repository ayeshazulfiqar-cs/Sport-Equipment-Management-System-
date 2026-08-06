package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.EquipmentRequest;
import com.sports.equipment.model.Transaction;
import com.sports.equipment.model.User;
import com.sports.equipment.service.EquipmentService;
import com.sports.equipment.service.RequestService;
import com.sports.equipment.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Window for returning equipment.
 */
public class ReturnEquipmentWindow {
    private TransactionService transactionService;
    private EquipmentService equipmentService;
    private RequestService requestService;

    public ReturnEquipmentWindow() {
        this.transactionService = SportsEquipmentApp.getTransactionService();
        this.equipmentService = SportsEquipmentApp.getEquipmentService();
        this.requestService = SportsEquipmentApp.getRequestService();
    }

    public void show(User user, BorderPane parentPane) {
        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("Return Equipment");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Active transactions
        List<Transaction> activeTransactions = transactionService
                .getUserTransactions(user.getId())
                .stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                .collect(Collectors.toList());

        TableView<Transaction> table = createTransactionsTable();
        table.getItems().addAll(activeTransactions);

        VBox returnForm = new VBox(10);
        returnForm.setPadding(new Insets(15));
        returnForm.setBorder(new javafx.scene.layout.Border(
                new javafx.scene.layout.BorderStroke(
                        javafx.scene.paint.Color.LIGHTGRAY,
                        javafx.scene.layout.BorderStrokeStyle.SOLID,
                        null,
                        new javafx.scene.layout.BorderWidths(1)
                )
        ));

        Label formLabel = new Label("Return Selected Equipment");
        formLabel.setStyle("-fx-font-weight: bold;");

        CheckBox damagedCheckbox = new CheckBox("Equipment is damaged");

        Label damageLabel = new Label();
        damagedCheckbox.setOnAction(e -> {
            if (damagedCheckbox.isSelected()) {
                damageLabel.setText("Damage penalty: Rs. 50");
                damageLabel.setStyle("-fx-text-fill: red;");
            } else {
                damageLabel.setText("");
            }
        });

        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason for damage (if applicable)");

        Label messageLabel = new Label();

        Button returnButton = new Button("Return Equipment");

        returnButton.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                messageLabel.setText("Please select a transaction first");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            double penalty = transactionService.returnEquipment(
                    selected.getId(),
                    damagedCheckbox.isSelected()
            );

            equipmentService.updateAvailableQuantity(
                    selected.getEquipmentId(),
                    selected.getQuantity()
            );

            Optional<EquipmentRequest> request =
                    requestService.getRequestById(selected.getRequestId());

            request.ifPresent(r -> requestService.markAsReturned(r.getId()));

            messageLabel.setText("Returned successfully! Penalty: Rs. " + penalty);
            messageLabel.setStyle("-fx-text-fill: green;");

            List<Transaction> updated = transactionService
                    .getUserTransactions(user.getId())
                    .stream()
                    .filter(t -> t.getStatus() == Transaction.TransactionStatus.ISSUED)
                    .collect(Collectors.toList());

            table.getItems().setAll(updated);
        });

        returnForm.getChildren().addAll(
                formLabel,
                damagedCheckbox,
                damageLabel,
                new Label("Reason:"),
                reasonField,
                returnButton
        );

        contentArea.getChildren().addAll(
                titleLabel,
                new Label("Your Active Borrowings:"),
                table,
                new Separator(),
                returnForm,
                messageLabel
        );

        parentPane.setCenter(contentArea);
    }

    private TableView<Transaction> createTransactionsTable() {
        TableView<Transaction> table = new TableView<>();

        TableColumn<Transaction, String> idCol = new TableColumn<>("Transaction ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));

        TableColumn<Transaction, String> equipmentCol = new TableColumn<>("Equipment ID");
        equipmentCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEquipmentId()));

        TableColumn<Transaction, Integer> quantityCol = new TableColumn<>("Qty");
        quantityCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getQuantity()));

        TableColumn<Transaction, String> issueCol = new TableColumn<>("Issue Date");
        issueCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getIssueDate()));

        TableColumn<Transaction, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDueDate()));

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus().toString()));

        table.getColumns().addAll(idCol, equipmentCol, quantityCol, issueCol, dueCol, statusCol);
        table.setPrefHeight(300);

        return table;
    }
}
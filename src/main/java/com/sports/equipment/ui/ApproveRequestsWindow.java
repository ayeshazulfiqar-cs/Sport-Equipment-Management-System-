package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.EquipmentRequest;
import com.sports.equipment.model.User;
import com.sports.equipment.service.EquipmentService;
import com.sports.equipment.service.RequestService;
import com.sports.equipment.service.TransactionService;
import com.sports.equipment.util.IDGenerator;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Window for sports head to approve/reject requests.
 */
public class ApproveRequestsWindow {
    private RequestService requestService;
    private EquipmentService equipmentService;
    private TransactionService transactionService;

    public ApproveRequestsWindow() {
        this.requestService = SportsEquipmentApp.getRequestService();
        this.equipmentService = SportsEquipmentApp.getEquipmentService();
        this.transactionService = SportsEquipmentApp.getTransactionService();
    }

    public void show(User user, BorderPane parentPane) {
        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("Approve / Reject Requests");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Create table for pending requests
        TableView<EquipmentRequest> table = createRequestsTable();
        List<EquipmentRequest> pendingRequests = requestService.getPendingRequests();
        table.getItems().addAll(pendingRequests);

        // Action panel
        VBox actionPanel = new VBox(10);
        actionPanel.setPadding(new Insets(15));
        actionPanel.setStyle("-fx-border: 1px solid #ccc;");

        Label actionLabel = new Label("Action on Selected Request");
        actionLabel.setStyle("-fx-font-weight: bold;");

        Spinner<Integer> durationSpinner = new Spinner<>(1, 30, 7);
        durationSpinner.setPrefWidth(100);

        TextArea rejectionReasonArea = new TextArea();
        rejectionReasonArea.setPromptText("Rejection reason (if needed)");
        rejectionReasonArea.setPrefHeight(80);
        rejectionReasonArea.setWrapText(true);

        HBox buttonBox = new HBox(10);
        Button approveButton = new Button("Approve");
        approveButton.setStyle("-fx-padding: 8;");

        Button rejectButton = new Button("Reject");
        rejectButton.setStyle("-fx-padding: 8;");

        Label messageLabel = new Label();

        approveButton.setOnAction(e -> {
            EquipmentRequest selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                messageLabel.setText("Please select a request");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // Check equipment availability
            var equipment = equipmentService.getEquipmentById(selected.getEquipmentId());
            if (equipment.isEmpty() || equipment.get().getAvailableQuantity() < selected.getQuantity()) {
                messageLabel.setText("Equipment not available in requested quantity");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // Approve and create transaction
            requestService.approveRequest(selected.getId(), user.getId());

            // Issue equipment immediately
            String txnId = transactionService.issueEquipment(
                    selected.getId(),
                    selected.getUserId(),
                    selected.getEquipmentId(),
                    selected.getQuantity(),
                    durationSpinner.getValue(),
                    user.getId()
            );

            // Update equipment availability
            equipmentService.updateAvailableQuantity(selected.getEquipmentId(), -selected.getQuantity());
            requestService.markAsIssued(selected.getId());

            messageLabel.setText("Request approved and equipment issued! Transaction: " + txnId);
            messageLabel.setStyle("-fx-text-fill: green;");

            // Refresh table
            table.getItems().clear();
            List<EquipmentRequest> updated = requestService.getPendingRequests();
            table.getItems().addAll(updated);
        });

        rejectButton.setOnAction(e -> {
            EquipmentRequest selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                messageLabel.setText("Please select a request");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            String reason = rejectionReasonArea.getText();
            if (reason.isEmpty()) {
                messageLabel.setText("Please provide a rejection reason");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            requestService.rejectRequest(selected.getId(), reason, user.getId());
            messageLabel.setText("Request rejected");
            messageLabel.setStyle("-fx-text-fill: orange;");

            // Refresh table
            table.getItems().clear();
            List<EquipmentRequest> updated = requestService.getPendingRequests();
            table.getItems().addAll(updated);
        });

        buttonBox.getChildren().addAll(approveButton, rejectButton);

        actionPanel.getChildren().addAll(
                actionLabel,
                new Label("Loan Duration (days):"),
                durationSpinner,
                new Label("Rejection Reason (if rejecting):"),
                rejectionReasonArea,
                buttonBox,
                messageLabel
        );

        Label statusLabel = new Label("Pending requests: " + pendingRequests.size());

        contentArea.getChildren().addAll(
                titleLabel,
                statusLabel,
                table,
                new Separator(),
                actionPanel
        );

        parentPane.setCenter(contentArea);
    }

    private TableView<EquipmentRequest> createRequestsTable() {
        TableView<EquipmentRequest> table = new TableView<>();

        TableColumn<EquipmentRequest, String> idCol = new TableColumn<>("Request ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        idCol.setPrefWidth(100);

        TableColumn<EquipmentRequest, String> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserId()));
        userIdCol.setPrefWidth(100);

        TableColumn<EquipmentRequest, String> equipmentCol = new TableColumn<>("Equipment ID");
        equipmentCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEquipmentId()));
        equipmentCol.setPrefWidth(100);

        TableColumn<EquipmentRequest, Integer> quantityCol = new TableColumn<>("Qty");
        quantityCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getQuantity()));
        quantityCol.setPrefWidth(50);

        TableColumn<EquipmentRequest, String> dateCol = new TableColumn<>("Request Date");
        dateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRequestDate()));
        dateCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, userIdCol, equipmentCol, quantityCol, dateCol);
        table.setPrefHeight(350);

        return table;
    }
}

package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.EquipmentRequest;
import com.sports.equipment.model.User;
import com.sports.equipment.service.RequestService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Window to view user's equipment requests.
 */
public class MyRequestsWindow {

    private RequestService requestService;

    public MyRequestsWindow() {
        this.requestService = SportsEquipmentApp.getRequestService();
    }

    public void show(User user, BorderPane parentPane) {

        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("My Equipment Requests");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TableView<EquipmentRequest> table = createRequestsTable();

        List<EquipmentRequest> userRequests =
                requestService.getUserRequests(user.getId());

        table.getItems().addAll(userRequests);

        Label statusLabel =
                new Label("Total requests: " + userRequests.size());

        // ---------------- BUTTONS ----------------
        javafx.scene.layout.HBox buttonBox =
                new javafx.scene.layout.HBox(10);

        Button allButton = new Button("All");
        Button pendingButton = new Button("Pending");
        Button approvedButton = new Button("Approved");
        Button rejectedButton = new Button("Rejected");

        // ALL
        allButton.setOnAction(e -> {
            table.getItems().clear();
            List<EquipmentRequest> requests =
                    requestService.getUserRequests(user.getId());
            table.getItems().addAll(requests);
            statusLabel.setText("Showing " + requests.size() + " request(s)");
        });

        // PENDING
        pendingButton.setOnAction(e -> {
            table.getItems().clear();

            List<EquipmentRequest> requests =
                    requestService.getUserRequests(user.getId());

            List<EquipmentRequest> filtered = requests.stream()
                    .filter(r -> r.getStatus() == EquipmentRequest.RequestStatus.PENDING)
                    .collect(Collectors.toList());

            table.getItems().addAll(filtered);
            statusLabel.setText("Showing " + filtered.size() + " pending request(s)");
        });

        // APPROVED
        approvedButton.setOnAction(e -> {
            table.getItems().clear();

            List<EquipmentRequest> requests =
                    requestService.getUserRequests(user.getId());

            List<EquipmentRequest> filtered = requests.stream()
                    .filter(r -> r.getStatus() == EquipmentRequest.RequestStatus.APPROVED)
                    .collect(Collectors.toList());

            table.getItems().addAll(filtered);
            statusLabel.setText("Showing " + filtered.size() + " approved request(s)");
        });

        // REJECTED
        rejectedButton.setOnAction(e -> {
            table.getItems().clear();

            List<EquipmentRequest> requests =
                    requestService.getUserRequests(user.getId());

            List<EquipmentRequest> filtered = requests.stream()
                    .filter(r -> r.getStatus() == EquipmentRequest.RequestStatus.REJECTED)
                    .collect(Collectors.toList());

            table.getItems().addAll(filtered);
            statusLabel.setText("Showing " + filtered.size() + " rejected request(s)");
        });

        buttonBox.getChildren().addAll(
                allButton,
                pendingButton,
                approvedButton,
                rejectedButton
        );

        contentArea.getChildren().addAll(
                titleLabel,
                buttonBox,
                table,
                statusLabel
        );

        parentPane.setCenter(contentArea);
    }

    // ---------------- TABLE ----------------
    private TableView<EquipmentRequest> createRequestsTable() {

        TableView<EquipmentRequest> table = new TableView<>();

        TableColumn<EquipmentRequest, String> idCol =
                new TableColumn<>("Request ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getId()
                )
        );

        TableColumn<EquipmentRequest, String> equipmentCol =
                new TableColumn<>("Equipment ID");
        equipmentCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getEquipmentId()
                )
        );

        TableColumn<EquipmentRequest, Integer> quantityCol =
                new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getQuantity()
                )
        );

        TableColumn<EquipmentRequest, String> dateCol =
                new TableColumn<>("Request Date");
        dateCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getRequestDate()
                )
        );

        TableColumn<EquipmentRequest, String> statusCol =
                new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStatus().toString()
                )
        );

        TableColumn<EquipmentRequest, String> reasonCol =
                new TableColumn<>("Rejection Reason");
        reasonCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getRejectionReason() != null
                                ? c.getValue().getRejectionReason()
                                : "-"
                )
        );

        table.getColumns().addAll(
                idCol,
                equipmentCol,
                quantityCol,
                dateCol,
                statusCol,
                reasonCol
        );

        table.setPrefHeight(400);

        return table;
    }
}
package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.EquipmentRequest;
import com.sports.equipment.model.User;
import com.sports.equipment.service.RequestService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Window for sports head to view all requests.
 */
public class AllRequestsWindow {

    private RequestService requestService;

    public AllRequestsWindow() {
        this.requestService = SportsEquipmentApp.getRequestService();
    }

    public void show(User user, BorderPane parentPane) {

        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("All Equipment Requests");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Filter box
        HBox filterBox = new HBox(10);

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(
                "All Statuses",
                "PENDING",
                "APPROVED",
                "REJECTED",
                "ISSUED",
                "RETURNED"
        );
        statusCombo.setValue("All Statuses");

        Button filterButton = new Button("Filter");

        filterBox.getChildren().addAll(
                new Label("Status:"),
                statusCombo,
                filterButton
        );

        // Table
        TableView<EquipmentRequest> table = createRequestsTable();
        Label statusLabel = new Label();

        // Load all requests
        List<EquipmentRequest> allRequests = requestService.getAllRequests();
        table.getItems().addAll(allRequests);
        statusLabel.setText("Total requests: " + allRequests.size());

        // FILTER ACTION
        filterButton.setOnAction(e -> {

            String selectedStatus = statusCombo.getValue();
            List<EquipmentRequest> requests = requestService.getAllRequests();

            if (!selectedStatus.equals("All Statuses")) {

                requests = requests.stream()
                        .filter(r -> r.getStatus().toString().equals(selectedStatus))
                        .collect(Collectors.toList());
            }

            table.getItems().clear();
            table.getItems().addAll(requests);
            statusLabel.setText("Showing " + requests.size() + " request(s)");
        });

        contentArea.getChildren().addAll(
                titleLabel,
                filterBox,
                table,
                statusLabel
        );

        parentPane.setCenter(contentArea);
    }

    // ---------------- TABLE ----------------
    private TableView<EquipmentRequest> createRequestsTable() {

        TableView<EquipmentRequest> table = new TableView<>();

        TableColumn<EquipmentRequest, String> idCol = new TableColumn<>("Request ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getId())
        );

        TableColumn<EquipmentRequest, String> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getUserId())
        );

        TableColumn<EquipmentRequest, String> equipmentCol = new TableColumn<>("Equipment ID");
        equipmentCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEquipmentId())
        );

        TableColumn<EquipmentRequest, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getQuantity())
        );

        TableColumn<EquipmentRequest, String> dateCol = new TableColumn<>("Request Date");
        dateCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getRequestDate())
        );

        TableColumn<EquipmentRequest, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStatus().toString()
                )
        );

        table.getColumns().addAll(
                idCol,
                userIdCol,
                equipmentCol,
                qtyCol,
                dateCol,
                statusCol
        );

        table.setPrefHeight(450);
        return table;
    }
}
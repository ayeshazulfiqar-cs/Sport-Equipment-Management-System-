package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.Equipment;
import com.sports.equipment.model.User;
import com.sports.equipment.service.EquipmentService;
import com.sports.equipment.service.RequestService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Window for searching and requesting equipment.
 */
public class SearchEquipmentWindow {

    private EquipmentService equipmentService;
    private RequestService requestService;

    public SearchEquipmentWindow() {
        this.equipmentService = SportsEquipmentApp.getEquipmentService();
        this.requestService = SportsEquipmentApp.getRequestService();
    }

    public void show(User user, BorderPane parentPane) {

        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("Search Equipment");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // ---------------- SEARCH CONTROLS ----------------
        HBox searchBox = new HBox(10);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, category, or description");
        searchField.setPrefWidth(300);

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().add("All Categories");
        categoryCombo.getItems().addAll(equipmentService.getAllCategories());
        categoryCombo.setValue("All Categories");

        CheckBox availableOnly = new CheckBox("Available Only");
        availableOnly.setSelected(true);

        Button searchButton = new Button("Search");

        searchBox.getChildren().addAll(
                new Label("Search:"), searchField,
                new Label("Category:"), categoryCombo,
                availableOnly,
                searchButton
        );

        // ---------------- TABLE ----------------
        TableView<Equipment> table = createEquipmentTable();
        Label messageLabel = new Label();

        // ---------------- SEARCH ACTION ----------------
        searchButton.setOnAction(e -> {

            String keyword = searchField.getText();
            String category = categoryCombo.getValue();
            boolean onlyAvailable = availableOnly.isSelected();

            List<Equipment> results;

            if (!keyword.isEmpty()) {
                results = equipmentService.searchEquipment(keyword);
            } else {
                results = equipmentService.getAllEquipment();
            }

            // FILTER CATEGORY
            if (!category.equals("All Categories")) {
                results = results.stream()
                        .filter(eq -> eq.getCategory().equalsIgnoreCase(category))
                        .collect(Collectors.toList());
            }

            // FILTER AVAILABILITY
            if (onlyAvailable) {
                results = results.stream()
                        .filter(eq -> eq.getAvailableQuantity() > 0)
                        .collect(Collectors.toList());
            }

            table.getItems().clear();
            table.getItems().addAll(results);

            messageLabel.setText("Found " + results.size() + " equipment(s)");
        });

        // ---------------- REQUEST BUTTON ----------------
        HBox buttonBox = new HBox(10);

        Button requestButton = new Button("Request Selected Equipment");

        requestButton.setOnAction(e -> {

            Equipment selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                messageLabel.setText("Please select an equipment first");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Request Equipment");
            dialog.setHeaderText("Request: " + selected.getName());

            Spinner<Integer> quantitySpinner =
                    new Spinner<>(1, selected.getAvailableQuantity(), 1);

            VBox dialogContent = new VBox(10);
            dialogContent.setPadding(new Insets(10));
            dialogContent.getChildren().addAll(
                    new Label("Quantity:"),
                    quantitySpinner
            );

            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getButtonTypes().addAll(
                    ButtonType.OK,
                    ButtonType.CANCEL
            );

            if (dialog.showAndWait().isPresent()) {

                int quantity = quantitySpinner.getValue();

                String requestId = requestService.createRequest(
                        user.getId(),
                        selected.getId(),
                        quantity
                );

                messageLabel.setText("Request created successfully! ID: " + requestId);
                messageLabel.setStyle("-fx-text-fill: green;");
            }
        });

        buttonBox.getChildren().add(requestButton);

        // ---------------- INITIAL LOAD ----------------
        List<Equipment> allEquipment = equipmentService.getAvailableEquipment();
        table.getItems().addAll(allEquipment);

        messageLabel.setText("Loaded " + allEquipment.size() + " available equipment(s)");

        // ---------------- FINAL LAYOUT ----------------
        contentArea.getChildren().addAll(
                titleLabel,
                searchBox,
                table,
                buttonBox,
                messageLabel
        );

        parentPane.setCenter(contentArea);
    }

    // ---------------- TABLE CREATION ----------------
    private TableView<Equipment> createEquipmentTable() {

        TableView<Equipment> table = new TableView<>();

        TableColumn<Equipment, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getName())
        );

        TableColumn<Equipment, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory())
        );

        TableColumn<Equipment, Integer> qtyCol = new TableColumn<>("Available");
        qtyCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getAvailableQuantity()
                )
        );

        TableColumn<Equipment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStatus().toString()
                )
        );

        TableColumn<Equipment, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getDescription()
                )
        );

        table.getColumns().addAll(
                nameCol,
                categoryCol,
                qtyCol,
                statusCol,
                descCol
        );

        table.setPrefHeight(400);

        return table;
    }
}
package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.Equipment;
import com.sports.equipment.model.User;
import com.sports.equipment.service.EquipmentService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Window for sports head to view and manage inventory.
 */
public class InventoryWindow {

    private EquipmentService equipmentService;

    public InventoryWindow() {
        this.equipmentService = SportsEquipmentApp.getEquipmentService();
    }

    public void show(User user, BorderPane parentPane) {

        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("Equipment Inventory");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // ---------------- FILTER CONTROLS ----------------
        HBox filterBox = new HBox(10);

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().add("All Categories");
        categoryCombo.getItems().addAll(equipmentService.getAllCategories());
        categoryCombo.setValue("All Categories");

        CheckBox availableOnly = new CheckBox("Available Only");

        Button filterButton = new Button("Filter");

        filterBox.getChildren().addAll(
                new Label("Category:"),
                categoryCombo,
                availableOnly,
                filterButton
        );

        // ---------------- TABLE ----------------
        TableView<Equipment> table = createEquipmentTable();
        Label statusLabel = new Label();

        List<Equipment> allEquipment = equipmentService.getAllEquipment();
        table.getItems().addAll(allEquipment);
        statusLabel.setText("Total equipment: " + allEquipment.size());

        // ---------------- FILTER ACTION ----------------
        filterButton.setOnAction(e -> {

            String category = categoryCombo.getValue();
            boolean onlyAvailable = availableOnly.isSelected();

            List<Equipment> filtered = equipmentService.getAllEquipment();

            if (!category.equals("All Categories")) {

                filtered = filtered.stream()
                        .filter(eq -> eq.getCategory().equalsIgnoreCase(category))
                        .collect(Collectors.toList());
            }

            if (onlyAvailable) {

                filtered = filtered.stream()
                        .filter(eq -> eq.getAvailableQuantity() > 0)
                        .collect(Collectors.toList());
            }

            table.getItems().clear();
            table.getItems().addAll(filtered);

            statusLabel.setText("Showing " + filtered.size() + " equipment");
        });

        // ---------------- ACTION BUTTONS ----------------
        HBox actionBox = new HBox(10);

        Button deleteButton = new Button("Delete Selected");
        Button editButton = new Button("Edit Selected");

        deleteButton.setOnAction(e -> {

            Equipment selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Equipment");
                confirm.setContentText("Delete: " + selected.getName() + "?");

                if (confirm.showAndWait().get() == ButtonType.OK) {
                    equipmentService.deleteEquipment(selected.getId());
                    table.getItems().remove(selected);
                    statusLabel.setText("Equipment deleted");
                }
            }
        });

        editButton.setOnAction(e -> {

            Equipment selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {

                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Edit Equipment");

                Spinner<Integer> qty =
                        new Spinner<>(0, 1000, selected.getQuantity());

                ComboBox<Equipment.EquipmentStatus> status =
                        new ComboBox<>();

                status.getItems().addAll(Equipment.EquipmentStatus.values());
                status.setValue(selected.getStatus());

                VBox box = new VBox(10);
                box.setPadding(new Insets(10));
                box.getChildren().addAll(
                        new Label("Quantity:"), qty,
                        new Label("Status:"), status
                );

                dialog.getDialogPane().setContent(box);
                dialog.getDialogPane().getButtonTypes()
                        .addAll(ButtonType.OK, ButtonType.CANCEL);

                if (dialog.showAndWait().isPresent()) {
                    selected.setQuantity(qty.getValue());
                    selected.setStatus(status.getValue());
                    equipmentService.updateEquipment(selected);
                    table.refresh();
                }
            }
        });

        actionBox.getChildren().addAll(deleteButton, editButton);

        // ---------------- FINAL LAYOUT ----------------
        contentArea.getChildren().addAll(
                titleLabel,
                filterBox,
                table,
                actionBox,
                statusLabel
        );

        parentPane.setCenter(contentArea);
    }

    // ---------------- TABLE ----------------
    private TableView<Equipment> createEquipmentTable() {

        TableView<Equipment> table = new TableView<>();

        TableColumn<Equipment, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getId())
        );

        TableColumn<Equipment, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getName())
        );

        TableColumn<Equipment, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory())
        );

        TableColumn<Equipment, Integer> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getQuantity())
        );

        TableColumn<Equipment, Integer> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAvailableQuantity())
        );

        TableColumn<Equipment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStatus().toString()
                )
        );

        table.getColumns().addAll(
                idCol,
                nameCol,
                categoryCol,
                totalCol,
                availCol,
                statusCol
        );

        table.setPrefHeight(400);
        return table;
    }
}
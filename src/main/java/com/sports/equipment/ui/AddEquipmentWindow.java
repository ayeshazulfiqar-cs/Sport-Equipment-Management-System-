package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.User;
import com.sports.equipment.service.EquipmentService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Window for sports head to add new equipment.
 */
public class AddEquipmentWindow {
    private EquipmentService equipmentService;

    public AddEquipmentWindow() {
        this.equipmentService = SportsEquipmentApp.getEquipmentService();
    }

    public void show(User user, BorderPane parentPane) {
        VBox contentArea = new VBox(10);
        contentArea.setPadding(new Insets(20));

        Label titleLabel = new Label("Add New Equipment");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-border: 1px solid #ccc;");

        TextField nameField = new TextField();
        nameField.setPromptText("Equipment Name");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Football", "Cricket", "Basketball", "Volleyball", "Badminton", "Tennis", "Other");
        categoryCombo.setPromptText("Select Category");
        categoryCombo.setPrefWidth(200);

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 1000, 1);
        quantitySpinner.setPrefWidth(100);

        Button addButton = new Button("Add Equipment");
        addButton.setStyle("-fx-font-size: 14; -fx-padding: 8;");

        Label messageLabel = new Label();

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String category = categoryCombo.getValue();
            String description = descriptionField.getText();
            int quantity = quantitySpinner.getValue();

            if (name.isEmpty() || category == null || description.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            boolean added = equipmentService.addEquipment(name, category, description, quantity);
            if (added) {
                messageLabel.setText("Equipment added successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
                nameField.clear();
                categoryCombo.setValue(null);
                descriptionField.clear();
                quantitySpinner.getValueFactory().setValue(1);
            } else {
                messageLabel.setText("Failed to add equipment");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        formBox.getChildren().addAll(
                new Label("Equipment Name:"),
                nameField,
                new Label("Category:"),
                categoryCombo,
                new Label("Description:"),
                descriptionField,
                new Label("Quantity:"),
                quantitySpinner,
                addButton,
                messageLabel
        );

        contentArea.getChildren().addAll(
                titleLabel,
                formBox
        );

        parentPane.setCenter(contentArea);
    }
}

package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.User;
import com.sports.equipment.service.UserService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Login and Registration window.
 */
public class LoginWindow {

    private UserService userService;
    private Stage stage;

    public LoginWindow() {
        this.userService = SportsEquipmentApp.getUserService();
    }

    public void show(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Sports Equipment Management System");
        stage.setWidth(500);
        stage.setHeight(400);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(createLoginTab(), createRegisterTab());

        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- LOGIN TAB ----------------
    private Tab createLoginTab() {
        Tab tab = new Tab("Login");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label titleLabel = new Label("Login to System");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // ✔ FIXED: User instead of Optional<User>
            User user = userService.login(username, password);

            if (user != null) {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");

                javafx.application.Platform.runLater(() -> {
                    DashboardWindow dashboard = new DashboardWindow();
                    dashboard.show(user, stage);
                });

            } else {
                messageLabel.setText("Invalid username or password");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        vbox.getChildren().addAll(
                titleLabel,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                loginButton,
                messageLabel
        );

        tab.setContent(vbox);
        return tab;
    }

    // ---------------- REGISTER TAB ----------------
    private Tab createRegisterTab() {
        Tab tab = new Tab("Register");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label titleLabel = new Label("Create New Account");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        ComboBox<User.UserRole> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(
                User.UserRole.STUDENT,
                User.UserRole.TEACHER,
                User.UserRole.SPORTS_HEAD
        );
        roleCombo.setValue(User.UserRole.STUDENT);

        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            User.UserRole role = roleCombo.getValue();

            if (name.isEmpty() || email.isEmpty() ||
                    username.isEmpty() || password.isEmpty()) {

                messageLabel.setText("Please fill all fields");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // ✔ FIXED: register method now exists
            boolean registered = userService.register(
                    name, email, username, password, role
            );

            if (registered) {
                messageLabel.setText("Registration successful!");
                messageLabel.setStyle("-fx-text-fill: green;");

                nameField.clear();
                emailField.clear();
                usernameField.clear();
                passwordField.clear();

            } else {
                messageLabel.setText("Username already exists!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        vbox.getChildren().addAll(
                titleLabel,
                new Label("Full Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Role:"), roleCombo,
                registerButton,
                messageLabel
        );

        tab.setContent(vbox);
        return tab;
    }
}
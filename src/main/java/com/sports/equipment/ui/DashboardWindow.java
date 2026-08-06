package com.sports.equipment.ui;

import com.sports.equipment.SportsEquipmentApp;
import com.sports.equipment.model.User;
import com.sports.equipment.service.UserService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main dashboard window after login.
 */
public class DashboardWindow {
    private User currentUser;
    private UserService userService;
    private BorderPane borderPane;

    public DashboardWindow() {
        this.userService = SportsEquipmentApp.getUserService();
    }

    public void show(User user, Stage primaryStage) {
        this.currentUser = user;
        primaryStage.setTitle("Sports Equipment Management - " + user.getName());
        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);

        borderPane = new BorderPane();

        // Create navigation menu
        VBox menu = createMenu();
        borderPane.setLeft(menu);

        // Create content area
        VBox contentArea = createDashboardContent();
        borderPane.setCenter(contentArea);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color: #f0f0f0; -fx-border-right: 1px solid #ccc;");
        menu.setPrefWidth(200);

        Label userLabel = new Label("User: " + currentUser.getName());
        userLabel.setStyle("-fx-font-weight: bold;");

        Label roleLabel = new Label("Role: " + currentUser.getRole());
        roleLabel.setStyle("-fx-font-size: 11;");

        Separator sep1 = new Separator();

        Button searchButton = new Button("Search Equipment");
        searchButton.setPrefWidth(180);
        searchButton.setOnAction(e -> showSearchEquipment());

        Button myRequestsButton = new Button("My Requests");
        myRequestsButton.setPrefWidth(180);
        myRequestsButton.setOnAction(e -> showMyRequests());

        Button returnEquipmentButton = new Button("Return Equipment");
        returnEquipmentButton.setPrefWidth(180);
        returnEquipmentButton.setOnAction(e -> showReturnEquipment());

        Separator sep2 = new Separator();

        // Sports Head specific features
        if (currentUser.getRole() == User.UserRole.SPORTS_HEAD) {
            Button addEquipmentButton = new Button("Add Equipment");
            addEquipmentButton.setPrefWidth(180);
            addEquipmentButton.setOnAction(e -> showAddEquipment());

            Button approveRequestsButton = new Button("Approve Requests");
            approveRequestsButton.setPrefWidth(180);
            approveRequestsButton.setOnAction(e -> showApproveRequests());

            Button viewAllRequestsButton = new Button("View All Requests");
            viewAllRequestsButton.setPrefWidth(180);
            viewAllRequestsButton.setOnAction(e -> showAllRequests());

            Button inventoryButton = new Button("View Inventory");
            inventoryButton.setPrefWidth(180);
            inventoryButton.setOnAction(e -> showInventory());

            Button transactionsButton = new Button("View Transactions");
            transactionsButton.setPrefWidth(180);
            transactionsButton.setOnAction(e -> showTransactions());

            menu.getChildren().addAll(
                    userLabel,
                    roleLabel,
                    sep1,
                    searchButton,
                    myRequestsButton,
                    returnEquipmentButton,
                    sep2,
                    addEquipmentButton,
                    approveRequestsButton,
                    viewAllRequestsButton,
                    inventoryButton,
                    transactionsButton
            );
        } else {
            menu.getChildren().addAll(
                    userLabel,
                    roleLabel,
                    sep1,
                    searchButton,
                    myRequestsButton,
                    returnEquipmentButton
            );
        }

        Separator sep3 = new Separator();
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(180);
        logoutButton.setStyle("-fx-text-fill: red;");
        logoutButton.setOnAction(e -> logout());

        menu.getChildren().addAll(sep3, logoutButton);

        return menu;
    }

    private VBox createDashboardContent() {
        VBox content = new VBox();
        content.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label descLabel = new Label("Select an option from the menu on the left to get started.");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(welcomeLabel, descLabel);
        vbox.setPadding(new Insets(20));

        content.getChildren().add(vbox);
        return content;
    }

    private void showSearchEquipment() {
        SearchEquipmentWindow window = new SearchEquipmentWindow();
        window.show(currentUser, borderPane);
    }

    private void showMyRequests() {
        MyRequestsWindow window = new MyRequestsWindow();
        window.show(currentUser, borderPane);
    }

    private void showReturnEquipment() {
        ReturnEquipmentWindow window = new ReturnEquipmentWindow();
        window.show(currentUser, borderPane);
    }

    private void showAddEquipment() {
        AddEquipmentWindow window = new AddEquipmentWindow();
        window.show(currentUser, borderPane);
    }

    private void showApproveRequests() {
        ApproveRequestsWindow window = new ApproveRequestsWindow();
        window.show(currentUser, borderPane);
    }

    private void showAllRequests() {
        AllRequestsWindow window = new AllRequestsWindow();
        window.show(currentUser, borderPane);
    }

    private void showInventory() {
        InventoryWindow window = new InventoryWindow();
        window.show(currentUser, borderPane);
    }

    private void showTransactions() {
        TransactionsWindow window = new TransactionsWindow();
        window.show(currentUser, borderPane);
    }

    private void logout() {
        userService.logout();
        // Reload login window
        Stage stage = (Stage) borderPane.getScene().getWindow();
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.show(stage);
    }
}

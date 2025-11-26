package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    private String currentUserDisplayName = "Guest";
    private String currentUserRole = "Buyer"; // or "Seller"

    public static final double WIDTH = 1280;
    public static final double HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("LVJ - Simple Online Storefront");

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setResizable(false);

        showLoadingScreen();
        primaryStage.show();
    }

    /* ========= Navigation methods ========= */

    public void showLoadingScreen() {
        new LoadingScreen(this, primaryStage);
    }

    public void showWelcomeScreen() {
        new WelcomeScreen(this, primaryStage);
    }

    public void showLoginScreen() {
        new LoginScreen(this, primaryStage);
    }

    // Route to Buyer or Seller dashboards
    public void showDashboard() {
        if ("Seller".equals(currentUserRole)) {
            new SellerDashboard(this, primaryStage);
        } else {
            new BuyerDashboard(this, primaryStage);
        }
    }

    /* ========= User info access ========= */

    public void setCurrentUser(String displayName, String role) {
        this.currentUserDisplayName = displayName;
        this.currentUserRole = role;
    }

    public String getCurrentUserDisplayName() {
        return currentUserDisplayName;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    /* ========= Shared info dialog utility ========= */

    public void showInfoDialog(String title, String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

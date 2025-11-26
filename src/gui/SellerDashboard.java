package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SellerDashboard {

    public SellerDashboard(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        /* ===== Sidebar (Seller) ===== */
        VBox sideBar = new VBox(8);
        sideBar.getStyleClass().add("sidebar");
        sideBar.setPadding(new Insets(16));

        Label navLabel = new Label("Navigation");
        navLabel.getStyleClass().add("sidebar-title");

        Button storefrontBtn = new Button("My Storefront");
        storefrontBtn.getStyleClass().add("sidebar-button");
        storefrontBtn.setOnAction(e -> app.showInfoDialog("Coming soon",
                "Storefront screen UI will be shown here."));

        Button vouchersBtn = new Button("Manage Vouchers");
        vouchersBtn.getStyleClass().add("sidebar-button");
        vouchersBtn.setOnAction(e -> app.showInfoDialog("Coming soon",
                "Voucher management UI will be shown here."));

        Button transactionsBtn = new Button("Transaction Log");
        transactionsBtn.getStyleClass().add("sidebar-button");
        transactionsBtn.setOnAction(e -> app.showInfoDialog("Coming soon",
                "Transaction log UI will be shown here."));

        Button profileBtn = new Button("Profile");
        profileBtn.getStyleClass().add("sidebar-button");
        profileBtn.setOnAction(e -> app.showInfoDialog("Coming soon",
                "User profile screen UI will be shown here."));

        sideBar.getChildren().addAll(
                navLabel,
                storefrontBtn,
                vouchersBtn,
                transactionsBtn,
                profileBtn
        );

        /* ===== Main content ===== */
        VBox mainContent = new VBox(16);
        mainContent.setPadding(new Insets(24));
        mainContent.getStyleClass().add("dashboard-content");

        // Header row
        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label greeting = new Label("Good day, " + app.getCurrentUserDisplayName() + "!");
        greeting.getStyleClass().add("greeting-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label(
                "Logged in as: " + app.getCurrentUserDisplayName() + " (Seller)");
        userLabel.getStyleClass().add("user-label");

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("ghost-button");
        logoutBtn.setOnAction(e -> {
            app.setCurrentUser("Guest", "Buyer");
            app.showWelcomeScreen();
        });

        HBox rightHeader = new HBox(8, userLabel, logoutBtn);
        rightHeader.setAlignment(Pos.CENTER_RIGHT);

        headerRow.getChildren().addAll(greeting, spacer, rightHeader);

        // Description under header
        Label roleDescription = new Label("Here’s a quick overview of your storefront.");
        roleDescription.getStyleClass().add("subtitle");

        // Stats row (Seller)
        HBox statsRow = new HBox(12);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        VBox stat1 = createStatCard("Active Products", "12");
        VBox stat2 = createStatCard("Today’s Sales", "₱4,250.00");
        VBox stat3 = createStatCard("Available Vouchers", "4");

        statsRow.getChildren().addAll(stat1, stat2, stat3);

        // Recent activity
        VBox recentBox = new VBox(8);
        recentBox.getStyleClass().add("card");
        Label recentTitle = new Label("Recent Transactions");
        recentTitle.getStyleClass().add("card-title");

        Text recentPlaceholder = new Text(
                "No recent activity yet.\nOnce you start selling, your transactions will appear here.");
        recentPlaceholder.getStyleClass().add("card-body-text");

        recentBox.getChildren().addAll(recentTitle, recentPlaceholder);

        mainContent.getChildren().addAll(headerRow, roleDescription, statsRow, recentBox);

        root.setLeft(sideBar);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        stage.setScene(scene);
    }

    private VBox createStatCard(String label, String value) {
        VBox card = new VBox(4);
        card.getStyleClass().add("stat-card");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");

        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("stat-label");

        card.getChildren().addAll(valueLabel, textLabel);
        return card;
    }
}

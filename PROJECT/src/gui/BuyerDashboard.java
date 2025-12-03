package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import managers.CartManager;
import model.User;

public class BuyerDashboard {

    public BuyerDashboard(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        User currentUser = app.getCurrentUser(); // get logged-in user
        CartManager cart = app.getCurrentUserCart();

        /* ===== Sidebar ===== */
        VBox sideBar = new VBox(8);
        sideBar.getStyleClass().add("sidebar");
        sideBar.setPadding(new Insets(16));

        Label navLabel = new Label("Navigation");
        navLabel.getStyleClass().add("sidebar-title");

        Button browseBtn = new Button("Browse Storefront");
        browseBtn.getStyleClass().add("sidebar-button");
        browseBtn.setOnAction(e -> app.showStoreFrontScreen());

        Button cartBtn = new Button("My Cart");
        cartBtn.getStyleClass().add("sidebar-button");
        cartBtn.setOnAction(e -> app.showShoppingCartScreen());

        Button wishlistBtn = new Button("My Wishlist");
        wishlistBtn.getStyleClass().add("sidebar-button");
        wishlistBtn.setOnAction(e -> app.showInfoDialog("Coming soon",
                "Wishlist UI will be shown here."));

        Button profileBtn = new Button("Profile");
        profileBtn.getStyleClass().add("sidebar-button");
        profileBtn.setOnAction(e -> new ProfileScreen(app, stage));


        sideBar.getChildren().addAll(navLabel, browseBtn, cartBtn, wishlistBtn, profileBtn);

        /* ===== Main content ===== */
        VBox mainContent = new VBox(16);
        mainContent.setPadding(new Insets(24));
        mainContent.getStyleClass().add("dashboard-content");

        // Header row
        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label greeting = new Label("Good day, " + currentUser.getDisplayName() + "!");
        greeting.getStyleClass().add("greeting-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Logged in as: " + currentUser.getDisplayName() + " (Buyer)");
        userLabel.getStyleClass().add("user-label");

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("ghost-button");
        logoutBtn.setOnAction(e -> {
            app.setCurrentUser(null);
            app.showWelcomeScreen();
        });

        HBox rightHeader = new HBox(8, userLabel, logoutBtn);
        rightHeader.setAlignment(Pos.CENTER_RIGHT);

        headerRow.getChildren().addAll(greeting, spacer, rightHeader);

        // Description
        Label roleDescription = new Label("Here’s a quick overview of your marketplace.");
        roleDescription.getStyleClass().add("subtitle");

        // Stats row
        HBox statsRow = new HBox(12);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        VBox stat1 = createStatCard("Items in Cart", String.valueOf(cart.getItemCount()));
        VBox stat2 = createStatCard("Wishlist", "7 items"); // placeholder
        VBox stat3 = createStatCard("Available Vouchers", "2"); // placeholder

        statsRow.getChildren().addAll(stat1, stat2, stat3);

        // Recent activity
        VBox recentBox = new VBox(8);
        recentBox.getStyleClass().add("card");
        Label recentTitle = new Label("Recent Purchases");
        recentTitle.getStyleClass().add("card-title");

        Text recentPlaceholder = new Text(
                "No recent activity yet.\nOnce you start buying, your purchases will appear here.");
        recentPlaceholder.getStyleClass().add("card-body-text");

        recentBox.getChildren().addAll(recentTitle, recentPlaceholder);

        mainContent.getChildren().addAll(headerRow, roleDescription, statsRow, recentBox);

        root.setLeft(sideBar);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
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

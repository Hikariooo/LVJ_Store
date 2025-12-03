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
import model.Buyer;
import model.User;
import model.*;
import service.*;

public class BuyerDashboard {

    public BuyerDashboard(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        User currentUser = app.getCurrentUser(); // get logged-in user
        CartManager cart = app.getCurrentUserCart();
        Buyer buyer = (Buyer) currentUser;       // safe because this is BuyerDashboard
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
        
        // Vouchers
        Button viewVouchersBtn = new Button("View Store Vouchers");
        viewVouchersBtn.getStyleClass().add("sidebar-button");
        viewVouchersBtn.setOnAction(e -> new BuyerVoucherScreen(app, stage));

        Button wishlistBtn = new Button("My Wishlist");
        wishlistBtn.getStyleClass().add("sidebar-button");
        wishlistBtn.setOnAction(e -> new BuyerWishlistScreen(app, stage));


        Button profileBtn = new Button("Profile");
        profileBtn.getStyleClass().add("sidebar-button");
        profileBtn.setOnAction(e -> new ProfileScreen(app, stage));


        sideBar.getChildren().addAll(navLabel, browseBtn, cartBtn,viewVouchersBtn, wishlistBtn, profileBtn);

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

        // load buyer transaction history from file
        java.util.List<Transaction> buyerTxs = TransactionService.getBuyerHistory(buyer);

        int cartCount = cart.getItemCount();
        int wishlistCount = buyer.getWishlist() != null ? buyer.getWishlist().size() : 0;
        int purchaseCount = buyerTxs != null ? buyerTxs.size() : 0;

        VBox stat1 = createStatCard("Items in Cart", String.valueOf(cartCount));
        VBox stat2 = createStatCard("Wishlist Items", String.valueOf(wishlistCount));
        VBox stat3 = createStatCard("Total Purchases", String.valueOf(purchaseCount));

        statsRow.getChildren().addAll(stat1, stat2, stat3);

        // ================== Recent activity ==================
        VBox recentBox = new VBox(8);
        recentBox.getStyleClass().add("card");
        Label recentTitle = new Label("Recent Purchases");
        recentTitle.getStyleClass().add("card-title");

        if (buyerTxs == null || buyerTxs.isEmpty()) {
            Text recentPlaceholder = new Text(
                    "No recent activity yet.\nOnce you start buying, your purchases will appear here.");
            recentPlaceholder.getStyleClass().add("card-body-text");
            recentBox.getChildren().addAll(recentTitle, recentPlaceholder);
        } else {
            recentBox.getChildren().add(recentTitle);

            int shown = 0;
            for (int i = buyerTxs.size() - 1; i >= 0 && shown < 5; i--, shown++) {
                Transaction t = buyerTxs.get(i);
                String line = String.format(
                        "%s x%d — Original: ₱%.2f, Paid: ₱%.2f (with %s)",
                        t.getProductName(),
                        t.getQuantity(),
                        Math.abs(t.getOriginalPrice()),
                        Math.abs(t.getDiscountedPrice()),
                        t.getOtherUser().getDisplayName()
                );
                Label txnLabel = new Label(line);
                txnLabel.getStyleClass().add("card-body-text");
                recentBox.getChildren().add(txnLabel);
            }
        }


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

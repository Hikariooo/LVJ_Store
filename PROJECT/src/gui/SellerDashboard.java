package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import gui.VoucherManagementScreen;
import model.Seller;
import model.User;
import model.Voucher;
import model.Transaction;
import service.TransactionService;
import service.VoucherService;

import java.util.List;


public class SellerDashboard {

    public SellerDashboard(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        User currentUser = app.getCurrentUser(); // should be Seller
        Seller seller = (Seller) currentUser;
        
        /* ===== Sidebar (Seller) ===== */
        VBox sideBar = new VBox(8);
        sideBar.getStyleClass().add("sidebar");
        sideBar.setPadding(new Insets(16));

        Label navLabel = new Label("Navigation");
        navLabel.getStyleClass().add("sidebar-title");

        // Storefront
        Button storefrontBtn = new Button("My Storefront");
        storefrontBtn.getStyleClass().add("sidebar-button");
        storefrontBtn.setOnAction(e -> app.showStoreFrontScreen());

        // Vouchers
        Button vouchersBtn = new Button("Manage Vouchers");
        vouchersBtn.getStyleClass().add("sidebar-button");
        vouchersBtn.setOnAction(e -> {
            new VoucherManagementScreen(app, stage);
        });
        
     // Wishlist insights
        Button wishlistInsightsBtn = new Button("Wishlist Insights");
        wishlistInsightsBtn.getStyleClass().add("sidebar-button");
        wishlistInsightsBtn.setOnAction(e -> new SellerWishlistScreen(app, stage));

        
        // Transaction log
        Button transactionsBtn = new Button("Transaction Log");
        transactionsBtn.getStyleClass().add("sidebar-button");
        transactionsBtn.setOnAction(e -> new TransactionLogScreen(app, stage));

        // Profile
        Button profileBtn = new Button("Profile");
        profileBtn.getStyleClass().add("sidebar-button");
        profileBtn.setOnAction(e -> new ProfileScreen(app, stage));


        sideBar.getChildren().addAll(navLabel, storefrontBtn, vouchersBtn,wishlistInsightsBtn, transactionsBtn, profileBtn);

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

        Label userLabel = new Label("Logged in as: " + currentUser.getDisplayName() + " (Seller)");
        userLabel.getStyleClass().add("user-label");

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("ghost-button");
        logoutBtn.setOnAction(e -> {
            app.setCurrentUser(null); // logout
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

        int activeProducts = seller.getProductList() != null ? seller.getProductList().size() : 0;

     // load vouchers from file for this seller
     List<Voucher> vouchers = VoucherService.getSellerVouchers(seller);
     int voucherCount = vouchers != null ? vouchers.size() : 0;

     // Sum of all discountedPrice for this seller (positive values = money in)
     double totalSales = 0.0;
     List<Transaction> sellerTxs = TransactionService.getSellerHistory(seller);
     for (Transaction t : sellerTxs) {
         totalSales += t.getDiscountedPrice();
     }

     VBox stat1 = createStatCard("Active Products", String.valueOf(activeProducts));
     VBox stat2 = createStatCard("Total Sales", "₱" + String.format("%.2f", totalSales));
     VBox stat3 = createStatCard("Available Vouchers", String.valueOf(voucherCount));



        statsRow.getChildren().addAll(stat1, stat2, stat3);

        // Recent activity
     // Recent activity
        VBox recentBox = new VBox(8);
        recentBox.getStyleClass().add("card");
        Label recentTitle = new Label("Recent Transactions");
        recentTitle.getStyleClass().add("card-title");

        if (sellerTxs.isEmpty()) {
            Text recentPlaceholder = new Text(
                    "No recent activity yet.\nOnce you start selling, your transactions will appear here.");
            recentPlaceholder.getStyleClass().add("card-body-text");
            recentBox.getChildren().addAll(recentTitle, recentPlaceholder);
        } else {
            recentBox.getChildren().add(recentTitle);

            int shown = 0;
            for (int i = sellerTxs.size() - 1; i >= 0 && shown < 5; i--, shown++) {
                Transaction t = sellerTxs.get(i);
                String line = String.format(
                        "%s x%d — Earned: ₱%.2f (to %s)",
                        t.getProductName(),
                        t.getQuantity(),
                        t.getDiscountedPrice(),
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

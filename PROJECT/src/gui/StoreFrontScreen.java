package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import managers.ProductManager;
import managers.SellerManager;
import model.*;
import service.StoreService;

public class StoreFrontScreen {

    public StoreFrontScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0);

        User currentUser = app.getCurrentUser();

        // Title
        Label title = new Label("Storefront");
        title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);

        // Product Count
        int productCount = 0;
        if (currentUser instanceof Seller) {
            Seller seller = (Seller) currentUser;
            productCount = seller.getProductList().size();
        } else {
            productCount = ProductManager.getProducts().size();
        }
        Label productCountLabel = new Label("Available Products: " + productCount);
        productCountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        VBox topBox = new VBox(5, title, productCountLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        // Products Display
        VBox productsBox = new VBox(10);
        productsBox.setPadding(new Insets(15));
        productsBox.setAlignment(Pos.TOP_CENTER);

        for (Product product : ProductManager.getProducts()) {
            // Skip hidden products for buyers
            if (product.isHidden() && currentUser instanceof Buyer) continue;  // Skip hidden products for buyers

            HBox productRow = new HBox(10);

            // Seller can hide or unhide products
            if (currentUser instanceof Seller) {
                Button hideBtn = new Button(product.isHidden() ? "Unhide Product" : "Hide Product");
                hideBtn.getStyleClass().add("primary-button");

                hideBtn.setOnAction(e -> {
                    if (product.isHidden()) {
                        StoreService.unhideProduct(product);
                        app.showInfoDialog("Product Unhidden", product.getName() + " is now visible in the storefront.");
                    } else {
                        StoreService.hideProduct(product);
                        app.showInfoDialog("Product Hidden", product.getName() + " is now hidden from the storefront.");
                    }
                    // Save the product list after hiding/unhiding
                    SellerManager.saveProductsToFile((Seller) currentUser);
                    ProductManager.saveGlobalProducts();
                    app.showStoreFrontScreen(); // refresh
                });

                productRow.getChildren().add(hideBtn);
            }

            // Skip products that are hidden for buyers
            if (currentUser instanceof Buyer && product.isHidden()) continue; // Skip hidden products for buyers

            // Show only seller's products for sellers
            if (currentUser instanceof Seller && !product.getSeller().equals(currentUser)) continue;

            productRow.setAlignment(Pos.CENTER_LEFT);
            productRow.setPadding(new Insets(5));
            productRow.getStyleClass().add("product-row");

            // Name and Price of Product
            Label nameLabel = new Label(product.getName() + " - ₱" + product.getPrice());
            nameLabel.getStyleClass().add("product-name");

            // Remaining stock label
            Label stockLabel = new Label("Stock Left: " + product.getStock());
            stockLabel.getStyleClass().add("product-stock");

            // Add product name and stock label
            productRow.getChildren().addAll(nameLabel, stockLabel);

            // For Buyers: Add to Cart functionality
            if (currentUser instanceof Buyer) {
                Label sellerLabel = new Label("Seller: " + product.getSeller().getDisplayName());
                sellerLabel.getStyleClass().add("product-seller");

                Button addToCartBtn = new Button("Add to Cart");
                addToCartBtn.getStyleClass().add("primary-button");

                addToCartBtn.setOnAction(e -> {
                    if (product.getStock() > 0) {
                        app.getCurrentUserCart().addProduct(product);
                        app.showInfoDialog("Added", product.getName() + " added to your cart!");
                    } else {
                        app.showInfoDialog("Out of Stock", "Sorry, this product is out of stock.");
                    }
                });

                productRow.getChildren().addAll(sellerLabel, addToCartBtn);
            }

            // For Sellers: Remove product functionality
            else if (currentUser instanceof Seller) {
                Button removeBtn = new Button("Remove Product");
                removeBtn.getStyleClass().add("primary-button");

                removeBtn.setOnAction(e -> {
                    ProductManager.removeProduct(product);
                    SellerManager.saveProductsToFile((Seller) currentUser);
                    app.showInfoDialog("Removed", product.getName() + " has been removed!");
                    app.showStoreFrontScreen(); // refresh
                });

                productRow.getChildren().add(removeBtn);
            }

            productsBox.getChildren().add(productRow);
        }

        
        ScrollPane scrollPane = new ScrollPane(productsBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Navigation bar
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("primary-button");
        backBtn.setOnAction(e -> {
            if (currentUser instanceof Seller) app.showDashboard();
            else app.showBuyerDashboard();
        });

        navBar.getChildren().add(backBtn);

        if (currentUser instanceof Buyer) {
            Button cartBtn = new Button("Cart");
            cartBtn.getStyleClass().add("primary-button");
            cartBtn.setOnAction(e -> app.showShoppingCartScreen());
            navBar.getChildren().add(cartBtn);
        } else if (currentUser instanceof Seller) {
            Button addProductBtn = new Button("Add Product");
            addProductBtn.getStyleClass().add("primary-button");
            addProductBtn.setOnAction(e -> app.showAddProductScreen());
            navBar.getChildren().add(addProductBtn);
        }

        root.setBottom(navBar);

        // Scene
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);

        // Fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}

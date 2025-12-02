package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import managers.SellerManager;
import managers.ProductManager;
import model.Product;
import model.User;
import model.Buyer;
import model.Seller;

public class StoreFrontScreen {

    public StoreFrontScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0); // fade-in

        User currentUser = app.getCurrentUser();

        // Title
        Label title = new Label("Storefront");
        title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // Show number of products
        Label productCountLabel;

        if (currentUser instanceof Seller) {
            // Show only the products owned by the current seller
            int sellerProductCount = 0;
            for (Product product : ProductManager.getProducts()) {
                if (product.getSeller().equals(currentUser)) {
                    sellerProductCount++;
                }
            }
            productCountLabel = new Label("Available Products: " + sellerProductCount);
        } else {
            // For Buyer, show the total count of available products
            productCountLabel = new Label("Available Products: " + ProductManager.getProducts().size());
        }

        productCountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        VBox topBox = new VBox(5, title, productCountLabel);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        // Product display
        VBox productsBox = new VBox(10);
        productsBox.setPadding(new Insets(15));
        productsBox.setAlignment(Pos.TOP_CENTER);

        // Display products based on user type (Buyer or Seller)
        if (currentUser instanceof Buyer) {
            // Show all products for Buyers, including Seller's name
            for (Product product : ProductManager.getProducts()) {
                HBox productRow = new HBox(10);
                productRow.setAlignment(Pos.CENTER_LEFT);
                productRow.setPadding(new Insets(5));
                productRow.getStyleClass().add("product-row");

                Label nameLabel = new Label(product.getName() + " - ₱" + product.getPrice());
                nameLabel.getStyleClass().add("product-name");

                Label sellerLabel = new Label("Seller: " + product.getSeller().getDisplayName()); // Display the seller's name
                sellerLabel.getStyleClass().add("product-seller");

                Button addToCartBtn = new Button("Add to Cart");
                addToCartBtn.getStyleClass().add("primary-button");

                // Only allow buyers to add to cart
                addToCartBtn.setOnAction(e -> {
                    if (ProductManager.decrementProductStock(product, 1)) { // Decrement stock by 1
                        app.getCurrentUserCart().addProduct(product);
                        Seller seller = product.getSeller();
                        seller.receivePayment(product.getPrice()); // Increase seller's balance
                        SellerManager.saveProductsToFile(seller); // Update seller's product file
                        ProductManager.saveGlobalProducts(); // Update global product list
                        app.showInfoDialog("Added", product.getName() + " added to your cart!");
                    } else {
                        app.showInfoDialog("Out of Stock", "Sorry, this product is out of stock.");
                    }
                });

                productRow.getChildren().addAll(nameLabel, sellerLabel, addToCartBtn);
                productsBox.getChildren().add(productRow);
            }
        } // For sellers only: Display a 'Remove Product' button for each of their products
        else if (currentUser instanceof Seller) {
            // Show only the logged-in seller’s products
            for (Product product : ProductManager.getProducts()) {
                if (product.getSeller().equals(currentUser)) {  // Check if the product belongs to the current seller
                    HBox productRow = new HBox(10);
                    productRow.setAlignment(Pos.CENTER_LEFT);
                    productRow.setPadding(new Insets(5));
                    productRow.getStyleClass().add("product-row");

                    Label nameLabel = new Label(product.getName() + " - ₱" + product.getPrice());
                    nameLabel.getStyleClass().add("product-name");

                    Button removeBtn = new Button("Remove Product");
                    removeBtn.getStyleClass().add("primary-button");
                    removeBtn.setOnAction(e -> {
                        // Remove the product and update the seller’s file and global list
                        ProductManager.removeProduct(product); // Remove product from the global list
                        SellerManager.saveProductsToFile((Seller) currentUser); // Save updated seller’s products
                        app.showInfoDialog("Removed", product.getName() + " has been removed!");
                        app.showStoreFrontScreen(); // Refresh the store front screen
                    });

                    productRow.getChildren().addAll(nameLabel, removeBtn);
                    productsBox.getChildren().add(productRow);
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(productsBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Navigation buttons
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("primary-button");

        // Navigate back depending on user type
        backBtn.setOnAction(e -> {
            if (currentUser instanceof Seller) {
                app.showDashboard(); // shows SellerDashboard
            } else {
                app.showBuyerDashboard();
            }
        });

        // Only buyers need a cart button
        if (currentUser instanceof Buyer) {
            Button cartBtn = new Button("Cart");
            cartBtn.getStyleClass().add("primary-button");
            cartBtn.setOnAction(e -> app.showShoppingCartScreen());
            navBar.getChildren().addAll(backBtn, cartBtn);
        }
        // If user is Seller, back + Add Product
        else if (currentUser instanceof Seller) {
            Button addProductBtn = new Button("Add Product");
            addProductBtn.getStyleClass().add("primary-button");
            addProductBtn.setOnAction(e -> app.showAddProductScreen());

            navBar.getChildren().addAll(backBtn, addProductBtn);
        }

        root.setBottom(navBar);

        // Scene
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);

        // Fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}

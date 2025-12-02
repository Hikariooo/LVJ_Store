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

        // Product display
        VBox productsBox = new VBox(10);
        productsBox.setPadding(new Insets(15));
        productsBox.setAlignment(Pos.TOP_CENTER);

        for (Product product : ProductManager.getProducts()) {
            HBox productRow = new HBox(10);
            productRow.setAlignment(Pos.CENTER_LEFT);
            productRow.setPadding(new Insets(5));
            productRow.getStyleClass().add("product-row");

            Label nameLabel = new Label(product.getName() + " - ₱" + product.getPrice());
            nameLabel.getStyleClass().add("product-name");

            Button addToCartBtn = new Button("Add to Cart");
            addToCartBtn.getStyleClass().add("primary-button");

            // Only allow buyers to add to cart
            if (currentUser instanceof Buyer) {
                addToCartBtn.setOnAction(e -> {
                    app.getCurrentUserCart().addProduct(product);
                    app.showInfoDialog("Added", product.getName() + " added to your cart!");
                });
            } else {
                // Sellers cannot add to cart
                addToCartBtn.setText("Buyers Only");
                addToCartBtn.setDisable(true);
            }

            productRow.getChildren().addAll(nameLabel, addToCartBtn);
            productsBox.getChildren().add(productRow);
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

package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import managers.CartManager;
import model.Product;

public class ShoppingCartScreen {

    public ShoppingCartScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        CartManager cart = app.getCurrentUserCart();

        // Title
        Label title = new Label("My Shopping Cart");
        title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // Product list
        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(15));
        cartBox.setAlignment(Pos.TOP_CENTER);

        if (cart.getProducts().isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty.");
            emptyLabel.getStyleClass().add("subtitle");
            cartBox.getChildren().add(emptyLabel);
        } else {
            for (Product product : cart.getProducts()) {
                HBox productRow = new HBox(10);
                productRow.setAlignment(Pos.CENTER_LEFT);
                productRow.setPadding(new Insets(5));
                productRow.getStyleClass().add("product-row");

                Label nameLabel = new Label(product.getName() + " - ₱" + product.getPrice());
                nameLabel.getStyleClass().add("product-name");

                Button removeBtn = new Button("Remove");
                removeBtn.getStyleClass().add("secondary-button");
                removeBtn.setOnAction(e -> {
                    cart.removeProduct(product);
                    app.showShoppingCartScreen(); // refresh screen
                });

                productRow.getChildren().addAll(nameLabel, removeBtn);
                cartBox.getChildren().add(productRow);
            }
        }

        ScrollPane scrollPane = new ScrollPane(cartBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        // Bottom section: total & buttons
        HBox bottomBar = new HBox(15);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total: ₱" + String.format("%.2f", cart.getTotalPrice()));
        totalLabel.getStyleClass().add("subtitle");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showBuyerDashboard());

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.getStyleClass().add("primary-button");
        checkoutBtn.setOnAction(e -> {
            if (cart.getProducts().isEmpty()) {
                app.showInfoDialog("Empty Cart", "Your cart is empty.");
            } else {
                app.showInfoDialog("Purchase Complete",
                        "Thank you for your purchase of " + cart.getItemCount() + " item(s)!");
                cart.clear();
                app.showShoppingCartScreen(); // refresh cart
            }
        });

        bottomBar.getChildren().addAll(backBtn, totalLabel, checkoutBtn);
        root.setBottom(bottomBar);

        // Scene
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);
    }
}

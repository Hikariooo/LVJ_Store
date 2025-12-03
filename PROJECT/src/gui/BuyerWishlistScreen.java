package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import managers.WishlistManager;
import model.Buyer;
import model.Product;
import service.CartService;

public class BuyerWishlistScreen {

    public BuyerWishlistScreen(Main app, Stage stage) {

        Buyer buyer = app.getCurrentBuyer();
        WishlistManager.loadWishlist(buyer);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setPadding(new Insets(20));

        // TOP
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setOnAction(e -> app.showBuyerDashboard());

        Label title = new Label("My Wishlist");
        title.getStyleClass().add("screen-title");

        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // CENTER: table of wishlisted products
        TableView<Product> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("₱%.2f", data.getValue().getPrice())
                ));

        TableColumn<Product, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getSeller() != null
                                ? data.getValue().getSeller().getDisplayName()
                                : "Unknown"
                ));

        table.getColumns().addAll(nameCol, priceCol, sellerCol);
        table.getItems().addAll(buyer.getWishlist());

        root.setCenter(table);

        // BOTTOM: buttons
        Button removeBtn = new Button("Remove from Wishlist");
        removeBtn.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                CartService.removeFromWishlist(buyer, selected);
                table.getItems().remove(selected);
            }
        });

        Button moveToCartBtn = new Button("Move to Cart");
        moveToCartBtn.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // add to cart, keep or remove from wishlist (your choice)
                CartService.addToCart(buyer, app.getCurrentUserCart(), selected);
                CartService.removeFromWishlist(buyer, selected);
                table.getItems().remove(selected);
                app.showInfoDialog("Added", "Item moved to cart.");
            }
        });

        HBox bottomBar = new HBox(10, removeBtn, moveToCartBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));

        root.setBottom(bottomBar);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }
}

package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import managers.CartManager;
import service.CartService;
import service.BalanceService;
import model.Product;
import model.Buyer;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartScreen {

    public ShoppingCartScreen(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        CartManager cart = app.getCurrentUserCart();
        Buyer buyer = app.getCurrentBuyer();

        // ================== TITLE ==================
        Label title = new Label("My Shopping Cart");
        title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // ================== BALANCE SECTION ==================
        HBox balanceBox = new HBox(10);
        balanceBox.setPadding(new Insets(10));
        balanceBox.setAlignment(Pos.CENTER_LEFT);

        Label balanceLabel = new Label("Current Balance: ₱" + String.format("%.2f", buyer.getBalance()));

        Button topUpBtn = new Button("Top-Up");
        topUpBtn.setOnAction(e -> {
            // Open Top-Up Window
            Stage topUpStage = new Stage();
            topUpStage.initOwner(stage);
            topUpStage.initModality(Modality.APPLICATION_MODAL);
            topUpStage.setTitle("Add Balance");

            VBox topUpBox = new VBox(10);
            topUpBox.setPadding(new Insets(15));
            topUpBox.setAlignment(Pos.CENTER);

            Label prompt = new Label("Enter amount to add:");
            TextField amountField = new TextField();
            amountField.setPromptText("Amount");

            Button confirmBtn = new Button("Confirm");
            confirmBtn.setOnAction(ev -> {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount > 0) {
                        BalanceService.topUpBuyer(buyer, amount);
                        balanceLabel.setText("Current Balance: ₱" + String.format("%.2f", buyer.getBalance()));
                        app.showInfoDialog("Success", "Balance topped up by ₱" + String.format("%.2f", amount));
                        topUpStage.close();
                    } else {
                        app.showInfoDialog("Invalid Amount", "Enter a positive number.");
                    }
                } catch (NumberFormatException ex) {
                    app.showInfoDialog("Invalid Input", "Please enter a valid number.");
                }
            });

            topUpBox.getChildren().addAll(prompt, amountField, confirmBtn);
            Scene topUpScene = new Scene(topUpBox, 300, 150);
            topUpStage.setScene(topUpScene);
            topUpStage.showAndWait();
        });

        balanceBox.getChildren().addAll(balanceLabel, topUpBtn);
        VBox topContainer = new VBox(10, balanceBox);
        root.setTop(topContainer);

        // ================== CART ITEMS ==================
        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(15));
        cartBox.setAlignment(Pos.TOP_CENTER);

        List<Product> selectedProducts = new ArrayList<>();

        if (cart.getProducts().isEmpty()) {
            cartBox.getChildren().add(new Label("Your cart is empty."));
        } else {
            for (Product product : cart.getProducts()) {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);

                CheckBox selectBox = new CheckBox();
                selectBox.setOnAction(e -> {
                    if (selectBox.isSelected()) selectedProducts.add(product);
                    else selectedProducts.remove(product);
                });

                Label name = new Label(product.getName() + " - ₱" + product.getPrice());

                Button remove = new Button("Remove");
                remove.setOnAction(e -> {
                    cart.removeProduct(product);
                    app.showShoppingCartScreen();
                });

                row.getChildren().addAll(selectBox, name, remove);
                cartBox.getChildren().add(row);
            }
        }

        ScrollPane scrollPane = new ScrollPane(cartBox);
        root.setCenter(scrollPane);

        // ================== BOTTOM BAR ==================
        Label totalLabel = new Label("Total: ₱" + String.format("%.2f", cart.getTotalPrice()));

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showBuyerDashboard());

        Button checkoutSelectedBtn = new Button("Checkout Selected");
        checkoutSelectedBtn.setOnAction(e -> {
            boolean success = CartService.checkoutSelected(buyer, cart, selectedProducts);
            if (!success) {
                app.showInfoDialog("Checkout Failed", "No items selected or insufficient balance.");
            } else {
                app.showInfoDialog("Success", "Selected items purchased!");
                app.showShoppingCartScreen();
            }
        });

        Button checkoutAllBtn = new Button("Checkout All");
        checkoutAllBtn.setOnAction(e -> {
            boolean success = CartService.checkoutAll(buyer, cart);
            if (!success) {
                app.showInfoDialog("Checkout Failed", "Your cart is empty or insufficient balance.");
            } else {
                app.showInfoDialog("Success", "All items purchased!");
                app.showShoppingCartScreen();
            }
        });

        HBox bottomBar = new HBox(15, backBtn, totalLabel, checkoutSelectedBtn, checkoutAllBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));
        root.setBottom(bottomBar);

        // ================== SCENE ==================
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
    }
}

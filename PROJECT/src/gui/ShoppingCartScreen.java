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
import service.VoucherService;
import model.Product;
import model.Buyer;
import model.Seller;
import model.Voucher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingCartScreen {

    public ShoppingCartScreen(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        CartManager cart = app.getCurrentUserCart();
        Buyer buyer = app.getCurrentBuyer();

        // ================== TITLE ==================
        Label title = new Label("My Shopping Cart");
        title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);

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

        VBox topContainer = new VBox(10, title, balanceBox);
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

        // ================== BOTTOM BAR (WITH VOUCHER) ==================
        Label totalLabel = new Label("Total: ₱" + String.format("%.2f", cart.getTotalPrice()));

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showBuyerDashboard());

        // --- Voucher controls ---
        Label voucherLabel = new Label("Voucher code:");
        TextField voucherField = new TextField();
        voucherField.setPromptText("Enter voucher name/code");
        Label voucherStatus = new Label();   // feedback text

        HBox voucherBox = new HBox(8, voucherLabel, voucherField);
        voucherBox.setAlignment(Pos.CENTER_LEFT);

        Button checkoutSelectedBtn = new Button("Checkout Selected");
        Button checkoutAllBtn = new Button("Checkout All");

        checkoutSelectedBtn.setOnAction(e -> {
            if (selectedProducts.isEmpty()) {
                app.showInfoDialog("Checkout Failed", "No items selected.");
                return;
            }

            List<Product> toBuy = new ArrayList<>(selectedProducts);
            Voucher voucher = resolveVoucherForProducts(toBuy, voucherField.getText(), voucherStatus, app);
            if (voucher == null && !voucherField.getText().trim().isEmpty()) {
                // invalid voucher; message already set in voucherStatus / dialog
                return;
            }

            boolean anySuccess = false;
            for (Product p : toBuy) {
                boolean ok = CartService.purchaseProduct(buyer, p, 1, voucher);
                if (ok) {
                    cart.removeProduct(p);
                    anySuccess = true;
                }
            }

            if (!anySuccess) {
                app.showInfoDialog("Checkout Failed", "Insufficient balance or stock.");
            } else {
                app.showInfoDialog("Success", "Selected items purchased!");
                app.showShoppingCartScreen();
            }
        });

        checkoutAllBtn.setOnAction(e -> {
            if (cart.getProducts().isEmpty()) {
                app.showInfoDialog("Checkout Failed", "Your cart is empty.");
                return;
            }

            List<Product> toBuy = new ArrayList<>(cart.getProducts());
            Voucher voucher = resolveVoucherForProducts(toBuy, voucherField.getText(), voucherStatus, app);
            if (voucher == null && !voucherField.getText().trim().isEmpty()) {
                // invalid voucher
                return;
            }

            boolean anySuccess = false;
            for (Product p : toBuy) {
                boolean ok = CartService.purchaseProduct(buyer, p, 1, voucher);
                if (ok) {
                    cart.removeProduct(p);
                    anySuccess = true;
                }
            }

            if (!anySuccess) {
                app.showInfoDialog("Checkout Failed", "Insufficient balance or stock.");
            } else {
                app.showInfoDialog("Success", "All items purchased!");
                app.showShoppingCartScreen();
            }
        });

        VBox bottomBox = new VBox(6);
        bottomBox.setPadding(new Insets(10));

        HBox buttonsRow = new HBox(15, backBtn, totalLabel, checkoutSelectedBtn, checkoutAllBtn);
        buttonsRow.setAlignment(Pos.CENTER);

        bottomBox.getChildren().addAll(voucherBox, voucherStatus, buttonsRow);
        root.setBottom(bottomBox);

        // ================== SCENE ==================
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Validates and resolves a voucher for the given products.
     * - Ensures all products are from the same seller when a code is provided.
     * - Looks up the voucher via VoucherService.
     * - Updates voucherStatus label / shows dialogs on failure.
     */
    private Voucher resolveVoucherForProducts(List<Product> products,
                                              String code,
                                              Label voucherStatus,
                                              Main app) {

        String trimmed = code == null ? "" : code.trim();
        if (trimmed.isEmpty()) {
            voucherStatus.setText("");
            return null; // no voucher used
        }

        // collect all sellers in the product list
        Set<Seller> sellers = new HashSet<>();
        for (Product p : products) {
            sellers.add(p.getSeller());
        }

        if (sellers.isEmpty()) {
            voucherStatus.setText("No items selected.");
            return null;
        }

        if (sellers.size() > 1) {
            String msg = "Voucher can only be used when all items are from the same seller.";
            voucherStatus.setText(msg);
            app.showInfoDialog("Voucher Error", msg);
            return null;
        }

        Seller onlySeller = sellers.iterator().next();
        Voucher voucher = VoucherService.findVoucher(onlySeller, trimmed);

        if (voucher == null) {
            String msg = "Voucher \"" + trimmed + "\" not found for this store.";
            voucherStatus.setText(msg);
            app.showInfoDialog("Voucher Error", msg);
        } else {
            voucherStatus.setText("Voucher applied: " + voucher.getName()
                    + " (" + voucher.getDiscountPercent() + "%, max ₱" + voucher.getMaxDiscount() + ")");
        }

        return voucher;
    }
}

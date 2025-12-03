package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import managers.ProductManager;
import model.Product;
import model.Seller;
import model.Voucher;
import service.VoucherService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BuyerVoucherScreen {

    public BuyerVoucherScreen(Main app, Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setPadding(new Insets(20));

        // ===== TOP BAR =====
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setOnAction(e -> new BuyerDashboard(app, stage)); // adjust class if different

        Label title = new Label("Store Vouchers");
        title.getStyleClass().add("screen-title");

        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // ===== CENTER: VOUCHER TABLE (ALL SELLERS) =====
        TableView<Voucher> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Voucher, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getSeller() != null
                                ? data.getValue().getSeller().getDisplayName()
                                : "Unknown"
                ));

        TableColumn<Voucher, String> nameCol = new TableColumn<>("Voucher");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Voucher, String> percentCol = new TableColumn<>("Discount (%)");
        percentCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("%.2f", data.getValue().getDiscountPercent())
                ));

        TableColumn<Voucher, String> maxCol = new TableColumn<>("Max Discount");
        maxCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("₱%.2f", data.getValue().getMaxDiscount())
                ));

        table.getColumns().addAll(sellerCol, nameCol, percentCol, maxCol);

        root.setCenter(table);

        // ===== LOAD ALL VOUCHERS FROM ALL SELLERS =====
        Set<Seller> sellers = new LinkedHashSet<>();
        for (Product p : ProductManager.getProducts()) {
            sellers.add(p.getSeller());
        }

        table.getItems().clear();
        for (Seller s : sellers) {
            List<Voucher> vouchers = VoucherService.getSellerVouchers(s);
            table.getItems().addAll(vouchers);
        }

        // ===== SCENE =====
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }
}

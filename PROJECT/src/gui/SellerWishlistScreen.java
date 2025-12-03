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

import java.io.*;
import java.util.*;

public class SellerWishlistScreen {

    private static final String WISHLIST_FOLDER = "src/storage/";

    public SellerWishlistScreen(Main app, Stage stage) {

        Seller seller = (Seller) app.getCurrentUser();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setPadding(new Insets(20));

        // TOP
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setOnAction(e -> new SellerDashboard(app, stage));

        Label title = new Label("Wishlist Insights");
        title.getStyleClass().add("screen-title");

        topBar.getChildren().addAll(backBtn, title);
        root.setTop(topBar);

        // CENTER TABLE
        TableView<WishRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<WishRow, String> productCol = new TableColumn<>("Product");
        productCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().productName()));

        TableColumn<WishRow, String> countCol = new TableColumn<>("Times Wishlisted");
        countCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().count())
                ));

        table.getColumns().addAll(productCol, countCol);

        // LOAD DATA
        List<WishRow> rows = computeWishlistCountsForSeller(seller);
        table.getItems().addAll(rows);

        root.setCenter(table);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }

    // Simple record-like helper (Java 16+ record syntax; replace with class if needed)
    public static class WishRow {
        private final String productName;
        private final int count;

        public WishRow(String productName, int count) {
            this.productName = productName;
            this.count = count;
        }

        public String productName() {
            return productName;
        }

        public int count() {
            return count;
        }
    }

    private List<WishRow> computeWishlistCountsForSeller(Seller seller) {
        Map<Integer, Integer> countByProductId = new HashMap<>();

        File dir = new File(WISHLIST_FOLDER);
        File[] files = dir.listFiles((d, name) -> name.endsWith("_wishlist.txt"));
        if (files == null) return Collections.emptyList();

        for (File f : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    try {
                        int productId = Integer.parseInt(line);
                        countByProductId.merge(productId, 1, Integer::sum);
                    } catch (NumberFormatException ignored) {}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Map product IDs to actual products belonging to this seller
        List<Product> allProducts = ProductManager.getProducts();
        Map<Integer, Product> productById = new HashMap<>();
        for (Product p : allProducts) {
            productById.put(p.getId(), p);
        }

        List<WishRow> rows = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : countByProductId.entrySet()) {
            int productId = entry.getKey();
            int count = entry.getValue();
            Product p = productById.get(productId);
            if (p != null && p.getSeller() != null &&
                p.getSeller().getUsername().equals(seller.getUsername())) {

                rows.add(new WishRow(p.getName(), count));
            }
        }

        // sort by count descending (optional)
        rows.sort((a, b) -> Integer.compare(b.count(), a.count()));
        return rows;
    }
}

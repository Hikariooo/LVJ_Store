package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Buyer;
import model.Seller;
import model.Transaction;
import model.User;
import service.TransactionService;

public class TransactionLogScreen {

    public TransactionLogScreen(Main app, Stage stage) {
    	User current = app.getCurrentUser();
    	java.util.List<Transaction> txs;
    	BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0);

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back to Storefront");
        backBtn.getStyleClass().add("primary-button");
        backBtn.setOnAction(e -> new StoreFrontScreen(app, stage)); // Corrected class name

        Label title = new Label("Transaction Log");
        title.getStyleClass().add("header-label");

        topBar.getChildren().addAll(backBtn, title);

        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(20));
        centerBox.setAlignment(Pos.TOP_LEFT);

        if (current instanceof Buyer) {
            txs = TransactionService.getBuyerHistory((Buyer) current);
        } else if (current instanceof Seller) {
            txs = TransactionService.getSellerHistory((Seller) current);
        } else {
            txs = java.util.Collections.emptyList();
        }

        if (txs.isEmpty()) {
            centerBox.getChildren().add(new Label("No transactions yet."));
        } else {
            for (Transaction t : txs) {
                Label txn = new Label(t.toString());
                txn.getStyleClass().add("field-label");
                centerBox.getChildren().add(txn);
            }
        }

        root.setTop(topBar);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}

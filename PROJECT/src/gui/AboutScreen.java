package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AboutScreen {

    public AboutScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0);

        // Back button - now goes to Welcome Screen
        Button backBtn = new Button("← Back to Welcome");
        backBtn.getStyleClass().add("primary-button");
        backBtn.setOnAction(e -> app.showWelcomeScreen());

        Label title = new Label("About LVJ Store");
        title.getStyleClass().add("header-label");

        Label info = new Label(
                "LVJ - Simple Online Storefront\n" +
                "This application allows you to buy and sell products easily.\n" +
                "Sellers can list their products, manage inventory, and track sales.\n" +
                "Buyers can browse products, add to cart, and make purchases.\n\n" +
                "Features:\n" +
                "• User authentication (Login/Signup)\n" +
                "• Product browsing\n" +
                "• Shopping cart functionality\n" +
                "• Seller dashboard\n" +
                "• Transaction history\n" +
                "• Balance management"
        );
        info.setWrapText(true);

        VBox centerBox = new VBox(15, title, info, backBtn);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

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
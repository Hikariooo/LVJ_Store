package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

    public WelcomeScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        VBox centerBox = new VBox(16);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getStyleClass().add("center-box");

        Label title = new Label("Welcome to LVJ");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("A simple online storefront for Buyers and Sellers.");
        subtitle.getStyleClass().add("subtitle");

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().addAll("primary-button", "large-button");
        loginBtn.setOnAction(e -> app.showLoginScreen());

        Button signupBuyerBtn = new Button("Sign up as Buyer");
        signupBuyerBtn.getStyleClass().addAll("secondary-button", "large-button");
        signupBuyerBtn.setOnAction(e ->
                app.showInfoDialog("Sign Up", "Sign up flow for Buyer goes here."));

        Button signupSellerBtn = new Button("Sign up as Seller");
        signupSellerBtn.getStyleClass().addAll("secondary-button", "large-button");
        signupSellerBtn.setOnAction(e ->
                app.showInfoDialog("Sign Up", "Sign up flow for Seller goes here."));

        buttonRow.getChildren().addAll(loginBtn, signupBuyerBtn, signupSellerBtn);
        centerBox.getChildren().addAll(title, subtitle, buttonRow);

        // Footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        Label footerText = new Label("CMSC 22 Mini Project • AY 2025-2026");
        footerText.getStyleClass().add("footer-text");
        footer.getChildren().add(footerText);

        root.setCenter(centerBox);
        root.setBottom(footer);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        stage.setScene(scene);
    }
}

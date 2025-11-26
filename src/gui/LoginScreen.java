package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginScreen {

    public LoginScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0); // for fade-in

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);

        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setMaxWidth(360);

        // Back to welcome
        Hyperlink backLink = new Hyperlink("← Back to welcome");
        backLink.getStyleClass().add("link-text");
        backLink.setOnAction(e -> app.showWelcomeScreen());

        Label cardTitle = new Label("Login to your account");
        cardTitle.getStyleClass().add("card-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("input-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("input-field");

        Label roleLabel = new Label("Login as:");
        roleLabel.getStyleClass().add("field-label");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Buyer", "Seller");
        roleCombo.getSelectionModel().selectFirst();
        roleCombo.getStyleClass().add("input-field");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-text");
        errorLabel.setVisible(false);

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("primary-button");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please enter both username and password.");
                errorLabel.setVisible(true);
                return;
            }

            app.setCurrentUser(user, roleCombo.getValue());
            app.showDashboard();
        });

        Hyperlink forgotLink = new Hyperlink("Forgot password?");
        forgotLink.getStyleClass().add("link-text");
        forgotLink.setOnAction(e -> app.showInfoDialog(
                "Forgot Password",
                "Password recovery flow goes here."));

        // Register row inside login card
        HBox registerRow = new HBox(5);
        registerRow.setAlignment(Pos.CENTER_RIGHT);

        Label noAccountLabel = new Label("No account yet?");
        noAccountLabel.getStyleClass().add("field-label");

        Hyperlink registerLink = new Hyperlink("Register now");
        registerLink.getStyleClass().add("link-text");
        // For now go back to welcome where user can pick Buyer/Seller sign-up
        registerLink.setOnAction(e -> app.showWelcomeScreen());

        registerRow.getChildren().addAll(noAccountLabel, registerLink);

        card.getChildren().addAll(
                backLink,
                cardTitle,
                usernameField,
                passwordField,
                roleLabel,
                roleCombo,
                errorLabel,
                loginBtn,
                forgotLink,
                registerRow
        );

        centerBox.getChildren().add(card);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        stage.setScene(scene);

        // fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}

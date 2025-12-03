package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import service.AuthService;

public class SignUpScreen {

    public SignUpScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0); // for fade-in animation

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);

        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setMaxWidth(360);

        // Back link
        Hyperlink backLink = new Hyperlink("← Back to welcome");
        backLink.getStyleClass().add("link-text");
        backLink.setOnAction(e -> app.showWelcomeScreen());

        Label cardTitle = new Label("Create your account");
        cardTitle.getStyleClass().add("card-title");

        // Form fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("input-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("input-field");

        TextField displayNameField = new TextField();
        displayNameField.setPromptText("Display Name");
        displayNameField.getStyleClass().add("input-field");

        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        locationField.getStyleClass().add("input-field");

        // Role selection
        Label roleLabel = new Label("Sign up as:");
        roleLabel.getStyleClass().add("field-label");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Buyer", "Seller");
        roleCombo.getSelectionModel().selectFirst();
        roleCombo.getStyleClass().add("input-field");

        // Error label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-text");
        errorLabel.setVisible(false);

        // Register button
        Button registerBtn = new Button("Sign Up");
        registerBtn.getStyleClass().add("primary-button");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String displayName = displayNameField.getText().trim();
            String location = locationField.getText().trim();
            String role = roleCombo.getValue();

            if (username.isEmpty() || password.isEmpty() || displayName.isEmpty() || location.isEmpty()) {
                errorLabel.setText("All fields are required.");
                errorLabel.setVisible(true);
                return;
            }

            User newUser = AuthService.register(username, password, role, displayName, location);

            if (newUser != null) {
                app.showInfoDialog("Success", "Account created! You can now log in.");
                app.showLoginScreen(); // go back to login screen after signup
            } else {
                errorLabel.setText("Registration failed. Username may already exist.");
                errorLabel.setVisible(true);
            }
        });

        // Assemble card
        card.getChildren().addAll(
                backLink,
                cardTitle,
                usernameField,
                passwordField,
                displayNameField,
                locationField,
                roleLabel,
                roleCombo,
                errorLabel,
                registerBtn
        );

        centerBox.getChildren().add(card);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);

        // Fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}

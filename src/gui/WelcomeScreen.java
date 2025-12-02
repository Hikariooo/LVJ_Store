package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeScreen {

    public WelcomeScreen(Main app, Stage stage) {
        // Get the scene from the stage instead of creating a new one
        Scene scene = stage.getScene();
        
        // Root layout for Welcome Screen
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.getStyleClass().add("welcome-screen");  // Apply gradient background
        
        // Start completely transparent
        root.setOpacity(0.0);

        // Load the logo image
        Image logoImage = new Image(getClass().getResource("/logo.png").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);

        // Set the size of the logo
        logoImageView.setFitWidth(200);  // Set the width
        logoImageView.setFitHeight(200); // Set the height
        logoImageView.setPreserveRatio(true); // Preserve aspect ratio

        // Create the center box for the login form
        VBox centerBox = new VBox(16);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getStyleClass().add("center-box");

        // Add the logo image to the center box
        centerBox.getChildren().add(logoImageView);

        // Add the "Login" and "Sign Up" buttons
        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().addAll("primary-button", "large-button");
        loginBtn.setOnAction(e -> {
            // Fade out transition and transition to Login Screen
            applyFadeOutTransition(root, app, stage, "login");
        });

        Button signupBtn = new Button("Sign Up");
        signupBtn.getStyleClass().addAll("secondary-button", "large-button");
        signupBtn.setOnAction(e -> {
            // Fade out transition and transition to Sign Up Screen
            applyFadeOutTransition(root, app, stage, "signup");
        });

        // Add the buttons to the center box
        centerBox.getChildren().addAll(loginBtn, signupBtn);

        // Add centerBox to the root layout
        root.setCenter(centerBox);

        // Set the root of the existing scene instead of creating a new scene
        scene.setRoot(root);

        // Apply fade-in transition to the Welcome Screen
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), root);  // 1-second fade-in
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // Method to apply the fade-out transition and then transition to the corresponding screen (Login or SignUp)
    private void applyFadeOutTransition(BorderPane root, Main app, Stage stage, String screenType) {
        // Fade-out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), root); // 1-second fade-out
        fadeOut.setFromValue(1.0);  // Start fully opaque
        fadeOut.setToValue(0.0);    // End fully transparent

        fadeOut.setOnFinished(ev -> {
            // Once the fade-out completes, show the appropriate screen
            if (screenType.equals("login")) {
                app.showLoginScreen(); // Transition to Login Screen
            } else if (screenType.equals("signup")) {
                app.showSignUpScreen(); // Transition to Sign Up Screen
            }
        });
        fadeOut.play();  // Start the fade-out transition
    }
}
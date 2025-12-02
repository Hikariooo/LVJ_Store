package gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadingScreen {

    public LoadingScreen(Main app, Stage stage) {
        // Get the scene from the stage instead of creating a new one
        Scene scene = stage.getScene();
        
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.getStyleClass().add("welcome-screen");

        // Load the image
        Image logoImage = new Image(getClass().getResource("/logo.png").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);

        // Set the size of the image
        logoImageView.setFitWidth(300);  // Set the width
        logoImageView.setFitHeight(300); // Set the height
        logoImageView.setPreserveRatio(true); // Preserve aspect ratio

        // Create a VBox to center the logo
        VBox centerBox = new VBox(16);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getStyleClass().add("center-box");

        // Add the logo image to the center box
        centerBox.getChildren().add(logoImageView);

        // Add the center box to the root layout
        root.setCenter(centerBox);

        // Set the root of the existing scene instead of creating a new scene
        scene.setRoot(root);

        // Delay before fading out to Welcome Screen
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            // Fade-out transition for the Loading Screen
            FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), root); // 1-second fade-out
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                // Transition to the Welcome Screen after fade-out completes
                app.showWelcomeScreen();
            });
            fadeOut.play();
        });
        pause.play();
    }
}
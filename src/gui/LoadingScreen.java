package gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadingScreen {

    public LoadingScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getStyleClass().add("center-box");

        Label logo = new Label("LVJ");
        logo.getStyleClass().add("app-title");

        Label loadingText = new Label("Loading, please wait...");
        loadingText.getStyleClass().add("subtitle");

        centerBox.getChildren().addAll(logo, loadingText);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("application.css NOT FOUND (check path/package)");
        }

        stage.setScene(scene);

        // Delay, then fade-out to Login
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> app.showLoginScreen());
            fadeOut.play();
        });
        pause.play();
    }
}

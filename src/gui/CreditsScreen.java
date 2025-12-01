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

public class CreditsScreen {

    public CreditsScreen(Main app, Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0);

        Button backBtn = new Button("← Back to Storefront");
        backBtn.getStyleClass().add("primary-button");
        backBtn.setOnAction(e -> new StoreFrontScreen(app, stage)); // Corrected class name

        Label title = new Label("Credits");
        title.getStyleClass().add("header-label");

        Label devs = new Label(
                "Developed by:\n" +
                "Jeremias P. Gomez\n" +
                "Vince Ilagan\n" +
                "Lawrence Maminta  \n"
        );
        devs.setWrapText(true);

        VBox centerBox = new VBox(15, title, devs, backBtn);
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

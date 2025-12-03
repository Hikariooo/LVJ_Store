package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import model.Buyer;
import model.Seller;
import service.AuthService;


public class ProfileScreen {

    public ProfileScreen(Main app, Stage stage) {

        User user = app.getCurrentUser();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("screen-root");
        root.setOpacity(0);

        /* ===== Title ===== */
        Label title = new Label("User Profile");
        title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        /* ===== Form ===== */
        GridPane form = new GridPane();
        form.setPadding(new Insets(25));
        form.setVgap(12);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);

        // Labels
        Label usernameLbl = new Label("Username:");
        Label roleLbl = new Label("Role:");
        Label displayNameLbl = new Label("Display Name:");
        Label locationLbl = new Label("Location:");
        Label balanceLbl = new Label("Balance:");

        // Values
        Text usernameTxt = new Text(user.getUsername());
        Text roleTxt = new Text(user instanceof Seller ? "Seller" : "Buyer");
        Text balanceTxt = new Text("₱" + user.getBalance());

        // Editable fields
        TextField displayNameField = new TextField(user.getDisplayName());
        TextField locationField = new TextField(user.getLocation());

        form.add(usernameLbl, 0, 0); form.add(usernameTxt, 1, 0);
        form.add(roleLbl, 0, 1);     form.add(roleTxt, 1, 1);
        form.add(displayNameLbl, 0, 2); form.add(displayNameField, 1, 2);
        form.add(locationLbl, 0, 3);    form.add(locationField, 1, 3);
        form.add(balanceLbl, 0, 4);     form.add(balanceTxt, 1, 4);

        root.setCenter(form);

        //buttons
        HBox buttonBar = new HBox(12);
        buttonBar.setPadding(new Insets(15));
        buttonBar.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("primary-button");

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("ghost-button");

        buttonBar.getChildren().addAll(backBtn, saveBtn);
        root.setBottom(buttonBar);

        //button logic

        saveBtn.setOnAction(e -> {

            String newName = displayNameField.getText().trim();
            String newLocation = locationField.getText().trim();

            if (newName.isEmpty() || newLocation.isEmpty()) {
                app.showInfoDialog("Invalid Input", "All fields are required.");
                return;
            }

            user.setDisplayName(newName);
            user.setLocation(newLocation);

            AuthService.saveProfileChanges(user); 

            app.showInfoDialog("Success", "Profile updated successfully!");
        });


        backBtn.setOnAction(e -> {
            if (user instanceof Seller) {
                app.showDashboard();   // SellerDashboard
            } else {
                app.showBuyerDashboard();
            }
        });

        //scene n animation
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}

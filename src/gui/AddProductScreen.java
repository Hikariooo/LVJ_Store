package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import managers.ProductManager;
import model.Product;
import model.User;
import model.Seller;

public class AddProductScreen {
	
	public AddProductScreen(Main app, Stage stage) {
		User currentUser = app.getCurrentUser();
		
		if(!(currentUser instanceof Seller)) {
			app.showInfoDialog("Access Denied", "Only Sellers can add products.");
			app.showStoreFrontScreen();
			return;
		}
		
		BorderPane root = new BorderPane();
		root.getStyleClass().add("screen-root");
		root.setOpacity(0);
		
		// Title
		Label title = new Label("Add Product (Type Details)");
		title.getStyleClass().add("screen-title");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);
        
        /* ===================== Form ===================== */
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));
        formGrid.setAlignment(Pos.CENTER);

        // Labels
        Label idLabel = new Label("Product ID:");
        Label nameLabel = new Label("Name:");
        Label categoryLabel = new Label("Category:");
        Label priceLabel = new Label("Price:");
        Label stockLabel = new Label("Stock:");

        // TextFields
        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField categoryField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();

        // Add to grid
        formGrid.add(idLabel, 0, 0);
        formGrid.add(idField, 1, 0);
        formGrid.add(nameLabel, 0, 1);
        formGrid.add(nameField, 1, 1);
        formGrid.add(categoryLabel, 0, 2);
        formGrid.add(categoryField, 1, 2);
        formGrid.add(priceLabel, 0, 3);
        formGrid.add(priceField, 1, 3);
        formGrid.add(stockLabel, 0, 4);
        formGrid.add(stockField, 1, 4);
        
        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("primary-button");
        
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        VBox centerBox = new VBox(15, formGrid, saveBtn, errorLabel);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);
        
        // Save button logic
        saveBtn.setOnAction(e -> {
        	// Trim all fields
            String idText = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String priceText = priceField.getText().trim();
            String stockText = stockField.getText().trim();

            // Check for empty fields
            if (idText.isEmpty() || name.isEmpty() || category.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                errorLabel.setText("All fields are required.");
                return;
            }
            
        	try {
        		int id = Integer.parseInt(idText);
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);
        		
        		Seller seller = (Seller) currentUser;
        		
        		Product p = new Product(id, name, category, price, stock, seller);
        		
        		// Add to manager and seller's list
                // ProductManager.addProduct(p);    <------ Replace this
        		seller.addProduct(p);
        		
        		app.showInfoDialog("Success", "Product added successfully!");
        		app.showStoreFrontScreen();
        		
        	} catch (NumberFormatException ex) {
        		errorLabel.setText("ID, Price, and Stock must be numbers.");
        	} catch (Exception ex) {
        		errorLabel.setText("Invalid input format. Please follow the example.");
        	}
        });

        /* ===================== Navigation Bar ===================== */
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(10));
        navBar.setAlignment(Pos.CENTER);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("primary-button");
        backBtn.setOnAction(e -> app.showStoreFrontScreen());

        navBar.getChildren().add(backBtn);
        root.setBottom(navBar);
        
        /* ===================== Scene Setup ===================== */
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        stage.setScene(scene);
        
        /* ===================== Fade-in Animation ===================== */
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
	}
}

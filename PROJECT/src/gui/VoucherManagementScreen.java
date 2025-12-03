package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import gui.Main;      // adjust if your Main class is in another package
import model.Seller;
import model.Voucher;
import service.VoucherService;

public class VoucherManagementScreen {

	public VoucherManagementScreen(Main app, Stage stage) {

		Seller seller = (Seller) app.getCurrentUser();

		BorderPane root = new BorderPane();
		root.getStyleClass().add("screen-root");
		root.setPadding(new Insets(20));

		// ===== TOP BAR =====
		HBox topBar = new HBox();
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setSpacing(10);

		Label title = new Label("Manage Vouchers");
		title.getStyleClass().add("screen-title");

		Button backBtn = new Button("← Back to Dashboard");
		backBtn.setOnAction(e -> new SellerDashboard(app, stage)); // adjust class name if needed

		topBar.getChildren().addAll(backBtn, title);
		root.setTop(topBar);

		// ===== CENTER: LIST OF VOUCHERS =====
		TableView<Voucher> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Voucher, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

		TableColumn<Voucher, String> percentCol = new TableColumn<>("Discount (%)");
		percentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
				String.format("%.2f", data.getValue().getDiscountPercent())
				));

		TableColumn<Voucher, String> maxCol = new TableColumn<>("Max Discount");
		maxCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
				String.format("₱%.2f", data.getValue().getMaxDiscount())
				));

		table.getColumns().addAll(nameCol, percentCol, maxCol);
		table.getItems().addAll(VoucherService.getSellerVouchers(seller));

		root.setCenter(table);

		// ===== BOTTOM: FORM + BUTTONS =====
		VBox bottomBox = new VBox(10);
		bottomBox.setPadding(new Insets(20, 0, 0, 0));

		HBox form = new HBox(10);
		form.setAlignment(Pos.CENTER_LEFT);

		TextField nameField = new TextField();
		nameField.setPromptText("Voucher name / code");

		TextField percentField = new TextField();
		percentField.setPromptText("Discount % (e.g. 10)");

		TextField maxField = new TextField();
		maxField.setPromptText("Max discount (e.g. 100)");

		Button addBtn = new Button("Add Voucher");
		Button removeBtn = new Button("Remove Selected");

		form.getChildren().addAll(nameField, percentField, maxField, addBtn, removeBtn);

		Label info = new Label("Vouchers apply only to items from this seller.");
		info.getStyleClass().add("subtitle");

		bottomBox.getChildren().addAll(form, info);
		root.setBottom(bottomBox);

		// ===== BUTTON HANDLERS =====
		addBtn.setOnAction(e -> {
			try {
				String name = nameField.getText();
				double percent = Double.parseDouble(percentField.getText());
				double max = Double.parseDouble(maxField.getText());

				if (name == null || name.isBlank()) {
					throw new IllegalArgumentException("Name required");
				}

				Voucher voucher = VoucherService.createVoucher(seller, name, percent, max);
				if (voucher != null) {
					table.getItems().clear();
					table.getItems().addAll(VoucherService.getSellerVouchers(seller));
					nameField.clear();
					percentField.clear();
					maxField.clear();
				}
			} catch (Exception ex) {
				new Alert(Alert.AlertType.ERROR, "Invalid voucher details.").showAndWait();
			}
		});

		removeBtn.setOnAction(e -> {
			Voucher selected = table.getSelectionModel().getSelectedItem();
			if (selected != null) {
				VoucherService.removeVoucher(seller, selected);
				table.getItems().remove(selected);
			}
		});

		// ===== SCENE =====
				Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

				// Reuse the same CSS as other screens
				var cssUrl = getClass().getResource("application.css");
				if (cssUrl != null) {
					scene.getStylesheets().add(cssUrl.toExternalForm());
				}

				stage.setScene(scene);
				stage.show();

	}
}

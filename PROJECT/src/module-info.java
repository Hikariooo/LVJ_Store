module PROJECT {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.media;
	
	opens gui to javafx.base, javafx.graphics, javafx.fxml;
}

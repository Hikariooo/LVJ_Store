package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import managers.CartManager;

public class Main extends Application {

    private Stage primaryStage;
    private User currentUser;   // currently logged-in user
    private CartManager currentUserCart; 
    public static final double WIDTH = 1280;
    public static final double HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("LVJ - Simple Online Storefront");

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setResizable(false);

        showLoadingScreen();
        primaryStage.show();
    }

    /* ========= Navigation Methods ========= */
    public void showLoadingScreen() { new LoadingScreen(this, primaryStage); }
    public void showWelcomeScreen() { new WelcomeScreen(this, primaryStage); }
    public void showLoginScreen() { new LoginScreen(this, primaryStage); }
    public void showSignUpScreen() { new SignUpScreen(this, primaryStage); }
    public void showShoppingCartScreen() { new ShoppingCartScreen(this, primaryStage); }
    public void showStoreFrontScreen() { new StoreFrontScreen(this, primaryStage); }
    public void showBuyerDashboard() { new BuyerDashboard(this, primaryStage); }
    public void showDashboard() {
        if (currentUser instanceof model.Seller) new SellerDashboard(this, primaryStage);
        else if (currentUser instanceof model.Buyer) new BuyerDashboard(this, primaryStage);
        else showWelcomeScreen();
    }

    /* ========= User Session ========= */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            this.currentUserCart = new CartManager();
        } else {
            this.currentUserCart = null;
        }
    }

    public User getCurrentUser() { return currentUser; }

    public CartManager getCurrentUserCart() {
        if (currentUserCart == null) currentUserCart = new CartManager();
        return currentUserCart;
    }

    /* ========= Shared Dialog Utility ========= */
    public void showInfoDialog(String title, String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}

package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Product;
import model.Seller;
import model.User;
import managers.CartManager;
<<<<<<< HEAD
import javafx.animation.PauseTransition;
import javafx.util.Duration;
=======
import managers.ProductManager;
import managers.SellerManager;
>>>>>>> branch 'master' of https://github.com/Hikariooo/LVJ_Store.git

public class Main extends Application {

    private Stage primaryStage;
    private Scene mainScene; // Single scene for the entire app
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

        // Create a single scene for the entire application
        mainScene = new Scene(new Pane(), WIDTH, HEIGHT);
        var cssUrl = getClass().getResource("application.css");
        if (cssUrl != null) {
            mainScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("application.css NOT FOUND (check path/package)");
        }
        primaryStage.setScene(mainScene);
        
        showLoadingScreen();
        primaryStage.show();
    }

    /* ========= Navigation Methods ========= */
    public void showLoadingScreen() { 
        new LoadingScreen(this, primaryStage); 
    }
    
    public void showWelcomeScreen() { 
        // Clear current scene content
        mainScene.setRoot(new Pane());
        
        // Small delay to ensure clean transition
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new WelcomeScreen(this, primaryStage);
        });
        delay.play();
    }
    
    public void showLoginScreen() { 
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new LoginScreen(this, primaryStage);
        });
        delay.play();
    }
    
    public void showSignUpScreen() { 
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new SignUpScreen(this, primaryStage);
        });
        delay.play();
    }
    
    public void showShoppingCartScreen() { 
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new ShoppingCartScreen(this, primaryStage);
        });
        delay.play();
    }
    
    public void showStoreFrontScreen() { 
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new StoreFrontScreen(this, primaryStage);
        });
        delay.play();
    }
    
    public void showAddProductScreen() {
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new AddProductScreen(this, primaryStage);
        });
        delay.play();
    }
    
    public void showBuyerDashboard() { 
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            new BuyerDashboard(this, primaryStage);
        });
        delay.play();
    }
    
    public void showDashboard() {
        mainScene.setRoot(new Pane());
        PauseTransition delay = new PauseTransition(Duration.millis(10));
        delay.setOnFinished(e -> {
            if (currentUser instanceof model.Seller) {
                new SellerDashboard(this, primaryStage);
            } else if (currentUser instanceof model.Buyer) {
                new BuyerDashboard(this, primaryStage);
            } else {
                showWelcomeScreen();
            }
        });
        delay.play();
    }

    /* ========= User Session ========= */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        
        if (user instanceof Seller) {
            Seller seller = (Seller) user;
            SellerManager.loadProducts(seller);  // Load seller products
            // Add all seller products to global ProductManager list
            for (Product p : seller.getProductList()) {
                ProductManager.addProduct(p);
            }
        }
        
        if (user != null) {
            this.currentUserCart = new CartManager(user.getUsername()); // ✅ user-based cart
        } else {
            this.currentUserCart = null;
        }
    }

    public User getCurrentUser() { return currentUser; }

    public CartManager getCurrentUserCart() {
        if (currentUserCart == null && currentUser != null) {
            currentUserCart = new CartManager(currentUser.getUsername());
        }
        return currentUserCart;
    }
    
    public model.Buyer getCurrentBuyer() {
        if (currentUser instanceof model.Buyer) {
            return (model.Buyer) currentUser;
        }
        return null;
    }

    @Override
    public void stop() {
        if (currentUserCart != null) {
            currentUserCart.saveCart();   
        }
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

    public static void main(String[] args) { 
        launch(args); 
    }
}
package model;

import java.util.ArrayList;
import java.util.List;

public class Buyer extends User {
    private static final long serialVersionUID = 1L;

    private List<Product> cart;
    private List<Product> wishlist;
    private List<Transaction> transactionLog;

    public Buyer(String username, String password, String displayName,
                 double balance, String location) {
        super(username, password, displayName, balance, location);
        this.cart = new ArrayList<>();
        this.wishlist = new ArrayList<>();
        this.transactionLog = new ArrayList<>();
    }

    /* ========= Cart Operations ========= */
    public void addToCart(Product product) { cart.add(product); }
    public void removeFromCart(Product product) { cart.remove(product); }
    public List<Product> getCart() { return cart; }

    /* ========= Wishlist Operations ========= */
    public void addToWishlist(Product product) { wishlist.add(product); }
    public void removeFromWishlist(Product product) { wishlist.remove(product); }
    public List<Product> getWishlist() { return wishlist; }

    /* ========= Transaction Log ========= */
    public void addTransaction(Transaction transaction) { transactionLog.add(transaction); }
    public List<Transaction> getTransactionLog() { return transactionLog; }

    /* ========= Balance Operations ========= */
    // Buyers can only add to balance
    public void topUp(double amount) {
        addBalance(amount);
    }
    
    
}

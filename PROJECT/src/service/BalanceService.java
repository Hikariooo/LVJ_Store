package service;

import model.Buyer;
import model.Seller;
import managers.*;

public class BalanceService {

    /* ==========================
       Buyer Top-Up
       ========================== */
    public static void topUpBuyer(Buyer buyer, double amount) {
        if (buyer == null || amount <= 0) return;

        buyer.addBalance(amount);   // updates User's balance
        UserManager.saveUsers();    // persist all users (including buyer)
    }
    
 // NEW: safely deduct from buyer and persist
    public static boolean deductFromBuyer(Buyer buyer, double amount) {
    	if (buyer == null || amount <= 0) return false;
        boolean ok = buyer.deductBalance(amount);
        if (!ok) return false;
        UserManager.saveUsers();   // persist updated balance
        return true;
    }

    /* ==========================
       Seller Receive Payment
       ========================== */
    // NEW: credit seller and persist
    public static void creditSeller(Seller seller, double amount) {
        if (seller == null || amount <= 0) return;
        seller.addBalance(amount);
        UserManager.saveUsers();   // persist updated balance
    }

    /* ==========================
       Check if Buyer has Sufficient Balance
       ========================== */
    public static boolean canAfford(Buyer buyer, double amount) {
        return buyer.getBalance() >= amount;
    }
    
    
}

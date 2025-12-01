package services;

import model.Buyer;
import model.Seller;

public class BalanceService {

    /* ==========================
       Buyer Top-Up
       ========================== */
    public static void topUpBuyer(Buyer buyer, double amount) {
        if (amount > 0) {
            buyer.addBalance(amount); // handled in User class
        }
    }

    /* ==========================
       Seller Receive Payment
       ========================== */
    public static void creditSeller(Seller seller, double amount) {
        if (amount > 0) {
            seller.addBalance(amount); // handled in User class
        }
    }

    /* ==========================
       Check if Buyer has Sufficient Balance
       ========================== */
    public static boolean canAfford(Buyer buyer, double amount) {
        return buyer.getBalance() >= amount;
    }
}

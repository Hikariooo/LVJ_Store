package service;

import managers.TransactionManager;
import model.Buyer;
import model.Seller;
import model.Transaction;
import model.Voucher;

import java.util.List;

public class TransactionService {

    /* =============================
       VIEW BUYER HISTORY
       ============================= */
    public static List<Transaction> getBuyerHistory(Buyer buyer) {
        if (buyer == null) return java.util.Collections.emptyList();
        // Load from file into buyer.transactionLog
        TransactionManager.loadTransactions(buyer);
        return buyer.getTransactionLog();
    }

    /* =============================
       VIEW SELLER HISTORY
       ============================= */
    public static List<Transaction> getSellerHistory(Seller seller) {
        if (seller == null) return java.util.Collections.emptyList();
        // Load from file into seller.transactionLog
        TransactionManager.loadTransactions(seller);
        return seller.getTransactionLog();
    }

    /* =============================
       VALIDATE VOUCHER USAGE
       ============================= */
    public static boolean validateVoucher(Voucher voucher, Seller seller) {
        return voucher.getSeller() == seller;
    }
}

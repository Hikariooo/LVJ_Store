package service;

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
        return buyer.getTransactionLog();
    }

    /* =============================
       VIEW SELLER HISTORY
       ============================= */
    public static List<Transaction> getSellerHistory(Seller seller) {
        return seller.getTransactionLog();
    }

    /* =============================
       VALIDATE VOUCHER USAGE
       ============================= */
    public static boolean validateVoucher(Voucher voucher, Seller seller) {
        return voucher.getSeller() == seller;
    }
}

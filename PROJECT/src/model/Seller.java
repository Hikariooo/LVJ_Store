package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import managers.*;

public class Seller extends User {
    private static final long serialVersionUID = 1L;
    //private double balance;
    private List<Product> productList;
    private List<Voucher> voucherList;
    private List<Transaction> transactionLog;

    public Seller(String username, String password, String displayName,
                  double balance, String location) {
        super(username, password, displayName, balance, location);
        this.productList = new ArrayList<>();
        this.voucherList = new ArrayList<>();
        this.transactionLog = new ArrayList<>();
    }

    /* ========= Product Operations ========= */
    public void addProduct(Product product) {
        productList.add(product);
    }

    public void removeProduct(Product product) {
        productList.remove(product);
    }

    public List<Product> getProductList() {
        return productList;
    }

    /* ========= Voucher Operations ========= */
    public void addVoucher(Voucher voucher) {
        voucherList.add(voucher);
    }

    public void removeVoucher(Voucher voucher) {
        voucherList.remove(voucher);
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    /* ========= Transaction Log ========= */
    public void addTransaction(Transaction transaction) {
        transactionLog.add(transaction);
    }

    public List<Transaction> getTransactionLog() {
        return transactionLog;
    }

    public void receivePayment(double amount) {
        // use User's balance
        addBalance(amount);          // <-- method from User

        // persist updated users (including this seller)
        UserManager.saveUsers();     // <-- use your actual method name here
    }

    public double getBalance() {
        return super.getBalance();
    }
}

package model;

public class Voucher {

    private String name;
    private Seller seller;
    private double discountPercent;
    private double maxDiscount;

    public Voucher(String name, Seller seller,
                   double discountPercent, double maxDiscount) {
        this.name = name;
        this.seller = seller;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
    }

    /* ========== Getters ========== */

    public String getName() {
        return name;
    }

    public Seller getSeller() {
        return seller;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    /* ========== Discount Logic ========== */

    public double applyDiscount(double originalPrice) {
        double discount = originalPrice * (discountPercent / 100.0);

        if (discount > maxDiscount) {
            discount = maxDiscount;
        }

        return originalPrice - discount;
    }
}

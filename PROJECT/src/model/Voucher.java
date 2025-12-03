package model;

import java.io.Serializable;

public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Seller seller;
    private double discountPercent;   // e.g. 10 = 10%
    private double maxDiscount;       // max peso discount

    public Voucher(String name, Seller seller,
                   double discountPercent, double maxDiscount) {
        this.name = name;
        this.seller = seller;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
    }

    /* ========== Getters / Setters ========== */

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

    public void setName(String name) {
        this.name = name;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    /* ========== Discount Logic ========== */

    /** Returns the discounted price (already minus the discount). */
    public double applyDiscount(double originalPrice) {
        double discount = originalPrice * (discountPercent / 100.0);

        if (discount > maxDiscount) {
            discount = maxDiscount;
        }

        return originalPrice - discount;
    }

    /* ========== File Helpers ========== */

    /** Format: name,percent,maxDiscount  (seller is inferred from filename) */
    public String toFileString() {
        return name + "," + discountPercent + "," + maxDiscount;
    }

    public static Voucher fromFileString(String line, Seller seller) {
        String[] parts = line.split(",");
        if (parts.length < 3) return null;

        String name = parts[0].trim();
        double percent = Double.parseDouble(parts[1].trim());
        double maxDisc = Double.parseDouble(parts[2].trim());

        return new Voucher(name, seller, percent, maxDisc);
    }

    @Override
    public String toString() {
        return name + " (" + discountPercent + "%, max ₱" + maxDiscount + ")";
    }
}

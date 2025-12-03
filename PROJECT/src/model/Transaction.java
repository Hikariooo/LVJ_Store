package model;

public class Transaction {

    private User otherUser;
    private String productName;
    private int quantity;
    private double originalPrice;
    private double discountedPrice;

    public Transaction(User otherUser, String productName,
                       int quantity, double originalPrice,
                       double discountedPrice) {
        this.otherUser = otherUser;
        this.productName = productName;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
    }

    /* ========== Getters ========== */

    public User getOtherUser() {
        return otherUser;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    /* ========== Display for Logs ========== */

    @Override
    public String toString() {
        return "With: " + otherUser.getDisplayName() +
               "\nProduct: " + productName +
               "\nQuantity: " + quantity +
               "\nOriginal: ₱" + originalPrice +
               "\nFinal: ₱" + discountedPrice;
    }
}

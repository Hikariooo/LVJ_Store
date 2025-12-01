package services;

import model.*;

public class CartService {

    /* =============================
       CART OPERATIONS
       ============================= */

    public static void addToCart(Buyer buyer, Product product) {
        buyer.addToCart(product);
    }

    public static void removeFromCart(Buyer buyer, Product product) {
        buyer.removeFromCart(product);
    }

    /* =============================
       WISHLIST OPERATIONS
       ============================= */

    public static void addToWishlist(Buyer buyer, Product product) {
        buyer.addToWishlist(product);
    }

    public static void removeFromWishlist(Buyer buyer, Product product) {
        buyer.removeFromWishlist(product);
    }

    /* =============================
       PURCHASE SINGLE PRODUCT
       ============================= */
    public static boolean purchaseProduct(Buyer buyer,
                                          Product product,
                                          int quantity,
                                          Voucher voucher) {

        // 1. Stock check
        if (product.getStock() < quantity) {
            return false;
        }

        double originalPrice = product.getPrice() * quantity;
        double finalPrice = originalPrice;

        // 2. Voucher validation
        if (voucher != null) {
            if (voucher.getSeller() == product.getSeller()) {
                finalPrice = voucher.applyDiscount(originalPrice);
            }
        }

        // 3. Balance check
        if (!buyer.deductBalance(finalPrice)) {
            return false;
        }

        // 4. Reduce stock
        product.decrementStock(quantity);

        // 5. Pay seller
        Seller seller = product.getSeller();
        seller.addBalance(finalPrice);

        // 6. Create logs
        Transaction buyerTxn = new Transaction(
                seller,
                product.getName(),
                quantity,
                -originalPrice,
                -finalPrice
        );

        Transaction sellerTxn = new Transaction(
                buyer,
                product.getName(),
                quantity,
                originalPrice,
                finalPrice
        );

        buyer.addTransaction(buyerTxn);
        seller.addTransaction(sellerTxn);

        return true;
    }
}

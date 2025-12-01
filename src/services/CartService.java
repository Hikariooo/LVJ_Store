package services;

import managers.CartManager;
import managers.TransactionManager;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    /* =============================
       ADD / REMOVE (Cart)
       - These use the CartManager instance for the logged-in user
       - CartManager handles persistence (save after changes)
       ============================= */

    public static void addToCart(Buyer buyer, CartManager cart, Product product) {
        if (buyer == null || cart == null || product == null) return;
        cart.addProduct(product);
        cart.saveCart(); // persist immediately
    }

    public static void removeFromCart(Buyer buyer, CartManager cart, Product product) {
        if (buyer == null || cart == null || product == null) return;
        cart.removeProduct(product);
        cart.saveCart(); // persist immediately
    }

    /* =============================
       Wishlist operations (Buyer-side)
       - These remain simple model operations
       ============================= */

    public static void addToWishlist(Buyer buyer, Product product) {
        if (buyer == null || product == null) return;
        buyer.addToWishlist(product);
        // If you want wishlist persistence, implement a WishlistManager similarly.
    }

    public static void removeFromWishlist(Buyer buyer, Product product) {
        if (buyer == null || product == null) return;
        buyer.removeFromWishlist(product);
    }

    /* =============================
       PURCHASE SINGLE PRODUCT (immediate buy)
       - Buyer buys a specific product (quantity + optional voucher)
       - Updates balances, stock, and transaction logs
       - Persists transaction via TransactionManager
       ============================= */

    public static boolean purchaseProduct(Buyer buyer,
                                          Product product,
                                          int quantity,
                                          Voucher voucher) {

        if (buyer == null || product == null || quantity <= 0) return false;

        // 1) Stock check
        if (product.getStock() < quantity) {
            return false;
        }

        double originalPrice = product.getPrice() * quantity;
        double finalPrice = originalPrice;

        // 2) Voucher validation (voucher applies only if voucher.seller == product.seller)
        if (voucher != null) {
            if (voucher.getSeller() != null && voucher.getSeller().getUsername().equals(product.getSeller().getUsername())) {
                finalPrice = voucher.applyDiscount(originalPrice);
            }
        }

        // 3) Balance check
        if (!buyer.deductBalance(finalPrice)) {
            return false;
        }

        // 4) Reduce stock
        boolean ok = product.decrementStock(quantity);
        if (!ok) {
            // rollback buyer balance (should not happen because we checked stock)
            buyer.addBalance(finalPrice);
            return false;
        }

        // 5) Pay seller
        Seller seller = product.getSeller();
        seller.addBalance(finalPrice);

        // 6) Create Transaction objects (positive values on receiver side)
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

        // 7) Persist transaction (create a temporary cart-like structure or call a tx manager)
        // We'll create a temp CartManager containing only this product to reuse your TransactionManager contract
        CartManager temp = new CartManager(buyer.getUsername());
        temp.clear(); // ensure empty
        // make a shallow copy product with quantity info reflected in price / or simply add repeated product entries
        // Simpler: add the same product `quantity` times
        for (int i = 0; i < quantity; i++) {
            temp.addProduct(product);
        }
        // Assuming TransactionManager has a method saveTransaction(User, CartManager)
        TransactionManager.saveTransaction(buyer, temp);

        return true;
    }

    /* =============================
       CHECKOUT SELECTED ITEMS
       - Checkout only the items chosen by the user (list of Product)
       - Removes these from cart, updates balances and transaction logs
       - Persists the cart after removals
       ============================= */

    public static boolean checkoutSelected(Buyer buyer,
                                           CartManager cart,
                                           List<Product> selectedProducts) {

        if (buyer == null || cart == null || selectedProducts == null || selectedProducts.isEmpty()) return false;

        // Calculate total
        double total = 0;
        for (Product p : selectedProducts) total += p.getPrice();

        // Balance check
        if (!buyer.deductBalance(total)) return false;

        // Process each product
        for (Product product : new ArrayList<>(selectedProducts)) {
            // reduce stock (if your business rules require stock decrement here)
            product.decrementStock(1); // using quantity 1 per stored product; adapt if you store quantity field

            // Pay seller
            Seller seller = product.getSeller();
            seller.addBalance(product.getPrice());

            // add transactions
            Transaction buyerTxn = new Transaction(seller, product.getName(), 1, -product.getPrice(), -product.getPrice());
            Transaction sellerTxn = new Transaction(buyer, product.getName(), 1, product.getPrice(), product.getPrice());
            buyer.addTransaction(buyerTxn);
            seller.addTransaction(sellerTxn);

            // remove from cart
            cart.removeProduct(product);
        }

        // Persist cart and save transaction (log purchased items)
        cart.saveCart();

        // Persist transaction log: create a temporary CartManager containing the selected products
        CartManager tmp = new CartManager(buyer.getUsername());
        tmp.clear();
        for (Product p : selectedProducts) tmp.addProduct(p);
        TransactionManager.saveTransaction(buyer, tmp);

        return true;
    }

    /* =============================
       CHECKOUT ALL (entire cart)
       - Similar to selected but operates on whole cart
       ============================= */

    public static boolean checkoutAll(Buyer buyer, CartManager cart) {
        if (buyer == null || cart == null) return false;
        if (cart.getProducts().isEmpty()) return false;

        double total = cart.getTotalPrice();

        // Balance check
        if (!buyer.deductBalance(total)) return false;

        // Process all products
        List<Product> productsToPurchase = new ArrayList<>(cart.getProducts());
        for (Product product : productsToPurchase) {

            product.decrementStock(1);

            Seller seller = product.getSeller();
            seller.addBalance(product.getPrice());

            Transaction buyerTxn = new Transaction(seller, product.getName(), 1, -product.getPrice(), -product.getPrice());
            Transaction sellerTxn = new Transaction(buyer, product.getName(), 1, product.getPrice(), product.getPrice());
            buyer.addTransaction(buyerTxn);
            seller.addTransaction(sellerTxn);
        }

        // Save transaction log (all items)
        CartManager tmp = new CartManager(buyer.getUsername());
        tmp.clear();
        for (Product p : productsToPurchase) tmp.addProduct(p);
        TransactionManager.saveTransaction(buyer, tmp);

        // Clear cart and persist empty cart
        cart.clear();
        cart.saveCart();

        return true;
    }
}

package service;

import managers.CartManager;
import managers.TransactionManager;
import managers.ProductManager;
import model.*;
import service.BalanceService;
import managers.UserManager;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    /* ========== ADD / REMOVE ========== */

    public static void addToCart(Buyer buyer, CartManager cart, Product product) {
        if (buyer == null || cart == null || product == null) return;
        cart.addProduct(product);
        cart.saveCart();
    }

    public static void removeFromCart(Buyer buyer, CartManager cart, Product product) {
        if (buyer == null || cart == null || product == null) return;
        cart.removeProduct(product);
        cart.saveCart();
    }

    /* ========== WISHLIST ========== */

    public static void addToWishlist(Buyer buyer, Product product) {
        if (buyer == null || product == null) return;
        buyer.addToWishlist(product);
    }

    public static void removeFromWishlist(Buyer buyer, Product product) {
        if (buyer == null || product == null) return;
        buyer.removeFromWishlist(product);
    }

    /* ========== BUY ONE PRODUCT (IMMEDIATE) ========== */

    
    
    public static boolean purchaseProduct(Buyer buyer,
            Product product,
            int quantity,
            Voucher voucher) {

    		if (buyer == null || product == null || quantity <= 0) return false;

    		// 1) Stock check
    		if (product.getStock() < quantity) return false;

    		double originalPrice = product.getPrice() * quantity;
    		double finalPrice = originalPrice;

    		// 2) Voucher (same seller only)
    		if (voucher != null &&
    					voucher.getSeller() != null &&
    					voucher.getSeller().getUsername().equals(product.getSeller().getUsername())) {

    		finalPrice = voucher.applyDiscount(originalPrice);
    }

    // 3) Check balance, then deduct using Buyer.reduceBalance (PERSISTS)
    if (!BalanceService.canAfford(buyer, finalPrice)) {
    	return false;
    }
    buyer.reduceBalance(finalPrice);   // <-- this calls UserManager.saveUsers()

    // 4) Decrement stock + credit seller (also persists seller + products)
    boolean ok = ProductManager.decrementProductStock(product, quantity);
    if (!ok) {
    // (optional) you could refund here using buyer.topUp(finalPrice);
    return false;
}

    Seller seller = product.getSeller();

// 5) Log transactions (in-memory)
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

// 6) Persist transaction batch (text file)
List<Product> purchased = new ArrayList<>();
for (int i = 0; i < quantity; i++) {
purchased.add(product);
}
TransactionManager.saveTransaction(buyer, purchased);

return true;
}


    /* ========== CHECKOUT SELECTED ========== */

    public static boolean checkoutSelected(Buyer buyer,
                                           CartManager cart,
                                           List<Product> selectedProducts) {

        if (buyer == null || cart == null || selectedProducts == null || selectedProducts.isEmpty())
            return false;

        // 1) Compute total
        double total = 0;
        for (Product p : selectedProducts) {
            total += p.getPrice();
        }

        // 2) Check if buyer can afford
        if (!BalanceService.canAfford(buyer, total)) return false;

        // 3) Deduct buyer balance ONCE (persists via reduceBalance)
        buyer.reduceBalance(total);     // <-- persists

        // 4) Decrement stock + credit sellers (saves products + sellers)
        for (Product product : new ArrayList<>(selectedProducts)) {
            boolean ok = ProductManager.decrementProductStock(product, 1);
            if (!ok) {
                // (optional rollback) but for now just fail
                return false;
            }
        }

        // 5) Log transactions & remove from cart
        for (Product product : new ArrayList<>(selectedProducts)) {
            Seller seller = product.getSeller();

            Transaction buyerTxn = new Transaction(
                    seller,
                    product.getName(),
                    1,
                    -product.getPrice(),
                    -product.getPrice()
            );

            Transaction sellerTxn = new Transaction(
                    buyer,
                    product.getName(),
                    1,
                    product.getPrice(),
                    product.getPrice()
            );

            buyer.addTransaction(buyerTxn);
            seller.addTransaction(sellerTxn);

            cart.removeProduct(product);
        }

        cart.saveCart();
        
        TransactionManager.saveTransaction(buyer, selectedProducts);

        return true;
    }

    /* ========== CHECKOUT ALL ========== */

    public static boolean checkoutAll(Buyer buyer, CartManager cart) {
        if (buyer == null || cart == null) return false;
        if (cart.getProducts().isEmpty()) return false;

        List<Product> productsToPurchase = new ArrayList<>(cart.getProducts());

        // 1) Compute total
        double total = 0;
        for (Product p : productsToPurchase) {
            total += p.getPrice();
        }

        // 2) Check if buyer can afford
        if (!BalanceService.canAfford(buyer, total)) return false;

        // 3) Deduct buyer balance ONCE (persists)
        buyer.reduceBalance(total);     // <-- persists

        // 4) Decrement stock + credit sellers (via ProductManager)
        for (Product product : productsToPurchase) {
            boolean ok = ProductManager.decrementProductStock(product, 1);
            if (!ok) return false;
        }

        // 5) Log transactions for all items
        for (Product product : productsToPurchase) {
            Seller seller = product.getSeller();

            Transaction buyerTxn = new Transaction(
                    seller,
                    product.getName(),
                    1,
                    -product.getPrice(),
                    -product.getPrice()
            );

            Transaction sellerTxn = new Transaction(
                    buyer,
                    product.getName(),
                    1,
                    product.getPrice(),
                    product.getPrice()
            );

            buyer.addTransaction(buyerTxn);
            seller.addTransaction(sellerTxn);
        }

        // 6) Save all purchased items as one transaction batch
        TransactionManager.saveTransaction(buyer, productsToPurchase);

        // 7) Clear cart and save
        cart.clear();
        cart.saveCart();

        return true;
    }
}

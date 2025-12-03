package service;

import model.Product;
import model.Seller;

import java.util.ArrayList;
import java.util.List;

public class StoreService {

    // Global storefront (all products from all sellers)
    private static final List<Product> globalProducts = new ArrayList<>();

    /* =============================
       ADD PRODUCT (SELLER)
       ============================= */
    public static void addProduct(Seller seller, Product product) {
        seller.addProduct(product);
        globalProducts.add(product);
    }

    /* =============================
       HIDE / UNHIDE PRODUCT
       ============================= */
    public static void hideProduct(Product product) {
        product.setHidden(true);
    }

    public static void unhideProduct(Product product) {
        product.setHidden(false);
    }

    /* =============================
       BROWSE ALL VISIBLE PRODUCTS
       ============================= */
    public static List<Product> browseVisibleProducts() {
        List<Product> visible = new ArrayList<>();

        for (Product p : globalProducts) {
            if (!p.isHidden() && p.getStock() > 0) {
                visible.add(p);
            }
        }
        return visible;
    }

    /* =============================
       GET PRODUCTS BY SELLER
       ============================= */
    public static List<Product> getProductsBySeller(Seller seller) {
        return seller.getProductList();
    }

    /* =============================
       FOR DEBUG / TESTING
       ============================= */
    public static List<Product> getAllProducts() {
        return globalProducts;
    }
}

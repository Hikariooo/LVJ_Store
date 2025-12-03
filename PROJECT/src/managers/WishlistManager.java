package managers;

import model.Buyer;
import model.Product;
import model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistManager {

    private static final String WISHLIST_FOLDER = "src/storage/";

    private static String getWishlistFileName(Buyer buyer) {
        return WISHLIST_FOLDER + buyer.getUsername() + "_wishlist.txt";
    }

    /* ========== SAVE ========== */

    public static void saveWishlist(Buyer buyer) {
        if (buyer == null) return;

        File dir = new File(WISHLIST_FOLDER);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(getWishlistFileName(buyer));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Product p : buyer.getWishlist()) {     // assumes Buyer.getWishlist()
                bw.write(p.getId() + "");               // save by product ID
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ========== LOAD ========== */

    public static void loadWishlist(Buyer buyer) {
        if (buyer == null) return;

        File file = new File(getWishlistFileName(buyer));
        buyer.getWishlist().clear();

        if (!file.exists()) return;

        List<Product> allProducts = ProductManager.getProducts();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    int productId = Integer.parseInt(line);
                    Product found = findProductById(allProducts, productId);
                    if (found != null) {
                        buyer.addToWishlist(found);
                    }
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Product findProductById(List<Product> products, int id) {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}

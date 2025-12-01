package managers;

import model.Product;
import model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private static final String PRODUCT_FILE = "src/storage/products.txt";
    private static List<Product> products = new ArrayList<>();

    static {
        products = loadProducts();
    }

    public static List<Product> getProducts() {
        return products;
    }

    public static void addProduct(Product product) {
        products.add(product);
        saveProducts();
    }

    /** Save products to text file */
    public static void saveProducts() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product p : products) {
                pw.println(p.getId() + "," +
                        p.getName() + "," +
                        p.getCategory() + "," +
                        p.getPrice() + "," +
                        p.getStock() + "," +
                        p.getSeller().getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Load products from text file */
    public static List<Product> loadProducts() {
        List<Product> loadedProducts = new ArrayList<>();
        File file = new File(PRODUCT_FILE);
        if (!file.exists()) return loadedProducts;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String category = parts[2];
                double price = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);
                String sellerUsername = parts[5];

                // Create Seller object for each product (simple version)
                Seller seller = new Seller(sellerUsername, "1234", sellerUsername, 0.0, "Unknown");

                Product p = new Product(id, name, category, price, stock, seller);
                loadedProducts.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedProducts;
    }


    public static Product findByName(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }
}

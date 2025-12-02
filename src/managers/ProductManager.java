package managers;

import model.Product;
import model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private static List<Product> products;

    static {
        products = loadGlobalProducts(); // Load global products initially
    }

    public static List<Product> getProducts() {
        return products;
    }

    // Save global product list to the main products.txt file
    public static void saveGlobalProducts() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("src/storage/products.txt"))) {
            for (Product product : products) {
                pw.println(product.getId() + "," +
                           product.getName() + "," +
                           product.getCategory() + "," +
                           product.getPrice() + "," +
                           product.getStock() + "," +
                           product.getSeller().getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load global products from the main products.txt file
    public static List<Product> loadGlobalProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/storage/products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue; // Skip malformed lines

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String category = parts[2];
                double price = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);
                String sellerUsername = parts[5];

                Seller seller = new Seller(sellerUsername, "dummyPassword", sellerUsername, 0.0, "Unknown"); // You can replace with actual seller
                Product product = new Product(id, name, category, price, stock, seller);
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Add a new product to the global list and save it
    public static void addProduct(Product product) {
        products.add(product);
        saveGlobalProducts(); // Save updated global products list
    }

    // Remove a product from the global list and save it
    public static void removeProduct(Product product) {
        products.remove(product);
        saveGlobalProducts(); // Save updated global products list
    }

    // Decrement stock of a product
    public static boolean decrementProductStock(Product product, int quantity) {
        // Check if the product has sufficient stock
        if (product.getStock() >= quantity) {
            product.decrementStock(quantity);  // Decrease stock of the product
            saveGlobalProducts();  // Save the updated global products list
            SellerManager.saveProductsToFile(product.getSeller());  // Save updated seller's product list
            return true;
        }
        return false;  // Not enough stock available
    }
}

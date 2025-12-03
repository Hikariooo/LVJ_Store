package managers;

import model.Product;
import model.Seller;
import model.Buyer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private static List<Product> products; // Global product list

    static {
        products = loadGlobalProducts(); // Load global products once on app start
    }

    public static List<Product> getProducts() {
        return products; // Return all products for display
    }

    // Save global product list to the main products.txt file
    public static void saveGlobalProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/storage/products.txt"))) {
            for (Product product : products) {
                writer.write(product.getId() + "," +
                             product.getName() + "," +
                             product.getCategory() + "," +
                             product.getPrice() + "," +
                             product.getStock() + "," +
                             product.getSeller().getUsername()+"," +
             				 product.isHidden()); 
                writer.newLine(); // Manually adding a new line after each product
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
                if (parts.length < 7) continue; // Skip malformed lines

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String category = parts[2];
                double price = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);
                String sellerUsername = parts[5];
                boolean hidden = Boolean.parseBoolean(parts[6]);  // Read hidden status

                // Check for duplicates based on product ID
                boolean exists = false;
                for (Product product : products) {
                    if (product.getId() == id) {
                        exists = true;
                        break;
                    }
                }

                // Only add non-duplicate products
                if (!exists) {
                    Seller seller = (Seller) UserManager.findByUsername(sellerUsername);
                    Product product = new Product(id, name, category, price, stock, seller);
                    product.setHidden(hidden);  // Apply the hidden status to the product
                    products.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }



    // Add a new product to the global list and save it
    public static void addProduct(Product product) {
        // Check if the product ID already exists in the global product list
        boolean exists = false;

        // Loop through the existing products in the global list and check for duplicates
        for (Product existingProduct : products) {
            if (existingProduct.getId() == product.getId()) {  // Compare IDs
                exists = true;  // Mark as duplicate if ID matches
                break;
            }
        }

        // If the product ID doesn't exist, add it to the global product list
        if (!exists) {
            products.add(product);  // Add the new product to the list
            saveGlobalProducts();   // Save the updated product list to the file
        } else {
            System.out.println("Product with ID " + product.getId() + " already exists.");
            // You can also show this error to the user via an alert or dialog if needed
        }
    }


    // Remove a product from the global list and save it
    public static void removeProduct(Product product) {
        products.remove(product);
        saveGlobalProducts();
    }

    // Decrement stock of a product
 // in ProductManager
    public static boolean decrementProductStock(Product product, int quantity) {
        if (product.getStock() >= quantity) {
            // 1) Decrement stock in memory
            product.decrementStock(quantity);
        
            // 2) Pay the seller
          /*  Seller seller = product.getSeller();
            if (seller != null) {
                seller.receivePayment(product.getPrice() * quantity);
                
            }*/
         // Save seller's products (and indirectly his state if you want)
            Seller seller = product.getSeller();
            SellerManager.saveProductsToFile(seller);
            // 3) Save updated product list to file
            saveGlobalProducts();
            return true;
        }
        return false;
    }

}

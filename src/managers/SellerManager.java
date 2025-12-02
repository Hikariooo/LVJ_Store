package managers;

import model.Product;
import model.Seller;
import java.io.*;


public class SellerManager {

    private static final String SELLER_FOLDER = "src/storage/productsofsellers/";

    // Save products of a specific seller to their own file
    public static void saveProductsToFile(Seller seller) {
        File dir = new File(SELLER_FOLDER);
        if (!dir.exists()) dir.mkdirs(); // Ensure directory exists

        File sellerFile = new File(SELLER_FOLDER + seller.getUsername() + "_products.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sellerFile))) {
            // Save the seller's products
            for (Product product : seller.getProductList()) {
                writer.write(product.getId() + "," +
                        product.getName() + "," +
                        product.getCategory() + "," +
                        product.getPrice() + "," +
                        product.getStock() + "," +
                        product.getSeller().getUsername());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load products from a seller's specific file
    public static void loadProducts(Seller seller) {
        File sellerFile = new File(SELLER_FOLDER + seller.getUsername() + "_products.txt");
        if (!sellerFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(sellerFile))) {
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

                // Correctly link the product to the existing seller object
                if (seller.getUsername().equals(sellerUsername)) {
                    Product product = new Product(id, name, category, price, stock, seller);
                    seller.addProduct(product);  // Add product to seller's product list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  


 
}

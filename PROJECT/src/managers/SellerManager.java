package managers;

import model.Product;
import model.Seller;

import java.io.*;
import java.util.List;

public class SellerManager {

	private static final String SELLER_FOLDER = "src/storage/";

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
	                         product.getSeller().getUsername() + "," +
	                         product.isHidden()); // Save hidden status
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	// Load products from a seller's specific file (not the global list)
	public static void loadProducts(Seller seller) {
	    File sellerFile = new File(SELLER_FOLDER + seller.getUsername() + "_products.txt");
	    if (!sellerFile.exists()) return;  // If no products file exists, return

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

	            // Check if the product already exists in the seller's list
	            boolean exists = false;
	            for (Product existingProduct : seller.getProductList()) {
	                if (existingProduct.getId() == id) {
	                    exists = true;
	                    break;
	                }
	            }

	            // Only add non-duplicate products
	            if (!exists) {
	                Product product = new Product(id, name, category, price, stock, seller);
	                seller.addProduct(product);  // Add the product to the seller’s list
	            } else {
	                System.out.println("Product with ID " + id + " already exists in the seller's product list.");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


}

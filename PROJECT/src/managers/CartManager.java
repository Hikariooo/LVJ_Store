package managers;

import model.Product;
import model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartManager implements Serializable {

    private List<Product> products;
    private String username;
    private static final String CART_FOLDER = "src/storage/";

    public CartManager(String username) {
        this.username = username;
        this.products = new ArrayList<>();
        loadCart();
    }

    /* ========= Cart Actions ========= */

    public void addProduct(Product product) {
        products.add(product);
        saveCart();
    }

    public void removeProduct(Product product) {
        products.remove(product);
        saveCart();
    }

    public void clear() {
        products.clear();
        saveCart();
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getItemCount() {
        return products.size();
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product p : products) total += p.getPrice();
        return total;
    }

    /* ========= File Handling ========= */

    private File getCartFile() {
        File dir = new File(CART_FOLDER);
        if (!dir.exists()) dir.mkdirs();
        return new File(CART_FOLDER + username + "_cart.txt");
    }

    public void saveCart() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(getCartFile()))) {
            for (Product p : products) {
                pw.println(
                        p.getId() + "," +
                        p.getName() + "," +
                        p.getCategory() + "," +
                        p.getPrice() + "," +
                        p.getStock() + "," +
                        p.getSeller().getUsername()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCart() {
        products.clear();
        File file = getCartFile();
        if (!file.exists()) return;

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

                Seller seller = new Seller(
                        sellerUsername, "1234", sellerUsername, 0.0, "Unknown"
                );

                Product product = new Product(id, name, category, price, stock, seller);
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

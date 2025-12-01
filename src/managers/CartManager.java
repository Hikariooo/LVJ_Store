package managers;

import model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartManager implements Serializable {
    private List<Product> products;

    public CartManager() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void clear() {
        products.clear();
    }

    public int getItemCount() {
        return products.size();
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product p : products) {
            total += p.getPrice();
        }
        return total;
    }
}

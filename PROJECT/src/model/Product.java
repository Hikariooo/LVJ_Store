package model;

import java.io.Serializable;

public class Product implements Serializable {

	private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private Seller seller;
    private boolean hidden;

    // Constructor with the ID provided by the user
    public Product(int id, String name, String category, double price, int stock, Seller seller) {
        this.id = id;  // ID is directly passed by the user
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.seller = seller;
    }

    // Getters and Setters for the attributes
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public Seller getSeller() {
        return seller;
    }

    public boolean isHidden() {
        return hidden;
    }

    /* ========== Setters ========== */

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        }
    }

    /* ========== Stock Control ========== */

    public boolean decrementStock(int quantity) {
        if (quantity > 0 && stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    public void addStock(int quantity) {
        if (quantity > 0) {
            stock += quantity;
        }
    }

    /* ========== Display ========== */

    public String getProductInfo() {
        return "ID: " + id +
               "\nName: " + name +
               "\nCategory: " + category +
               "\nPrice: ₱" + price +
               "\nStock: " + stock +
               "\nSeller: " + seller.getDisplayName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id == product.id; // Compare products based on their unique ID (or other attributes)
    }
    
    @Override
    public String toString() {
        return id + "," + name + "," + category + "," + price + "," + stock + "," + seller.getUsername();
    }

}

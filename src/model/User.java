package model;

import java.io.Serializable;

public abstract class User implements Serializable {
	private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String displayName;
    private double balance;
    private String location;

    public User(String username, String password, String displayName,
                double balance, String location) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.balance = balance;
        this.location = location;
    }

    //getters
    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getBalance() {
        return balance;
    }

    public String getLocation() {
        return location;
    }
    
    public String getPassword() {
        return password;
    }

    //setters
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

   
    //boolean operations
    public void addBalance(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean deductBalance(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    //walang password for security, ganon sa mga apps e hahaha
    public String viewStateInfo() {
        return "Username: " + username +
               "\nDisplay Name: " + displayName +
               "\nBalance: ₱" + balance +
               "\nLocation: " + location;
    }
}

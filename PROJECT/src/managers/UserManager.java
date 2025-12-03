package managers;

import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static final String USER_FILE = "src/storage/users.txt";
    private static List<User> users = new ArrayList<>();

    static {
        loadUsers();
    }

    public static List<User> getUsers() {
        return users;
    }

    public static User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public static void addUser(User user) {
        users.add(user);
        saveUsers();
    }
    
    public static void loadUsers() {
        users.clear();
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                String username = p[0];
                String password = p[1];
                String role = p[2];
                String displayName = p[3];
                double balance = Double.parseDouble(p[4]);
                String location = p[5];

                if (role.equals("SELLER")) {
                    users.add(new Seller(username, password, displayName, balance, location));
                } else {
                    users.add(new Buyer(username, password, displayName, balance, location));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                String role = (u instanceof Seller) ? "SELLER" : "BUYER";

                // Write each user's data
                writer.write(
                        u.getUsername() + "," +
                        u.getPassword() + "," +
                        role + "," +
                        u.getDisplayName() + "," +
                        u.getBalance() + "," +
                        u.getLocation()
                );
                writer.newLine();  // Manually add new line after each user
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveUsers(); //save immediately
    }

    
}

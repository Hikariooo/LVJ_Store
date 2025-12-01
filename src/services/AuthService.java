package services;

import managers.UserManager;
import model.User;
import model.Buyer;
import model.Seller;

public class AuthService {

    /** Attempt to log in a user with username, password, and role */
    public static User login(String username, String password, String role) {
        User user = UserManager.findByUsername(username);
        if (user != null && user.checkPassword(password)) {
            // Ensure the role matches the user type
            if ((role.equalsIgnoreCase("Buyer") && user instanceof Buyer) ||
                (role.equalsIgnoreCase("Seller") && user instanceof Seller)) {
                return user;
            }
        }
        return null; // invalid credentials or wrong role
    }

    /** Register a new user */
    public static User register(String username, String password, String role,
                                String displayName, String location) {
        // Check if username already exists
        if (UserManager.findByUsername(username) != null) {
            return null; // username taken
        }

        User newUser;
        if (role.equalsIgnoreCase("Buyer")) {
            newUser = new Buyer(username, password, displayName, 0, location);
        } else {
            newUser = new Seller(username, password, displayName, 0, location);
        }

        UserManager.addUser(newUser); // save immediately
        return newUser;
    }
    
    public static void saveProfileChanges(User user) {
        UserManager.updateUser(user);
    }
    
}

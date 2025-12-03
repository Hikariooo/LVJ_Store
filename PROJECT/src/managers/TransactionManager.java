package managers;

import model.Transaction;
import model.User;
import model.Buyer;
import model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private static final String TX_FOLDER = "src/storage/";

    /* ===========================
       SAVE ALL TRANSACTIONS
       =========================== */
    // Save the GIVEN list of transactions for one user
    public static void saveTransactions(User user, List<Transaction> txs) {
        if (user == null || txs == null) return;

        try {
            File dir = new File(TX_FOLDER);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(TX_FOLDER + user.getUsername() + "_transactions.txt");

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (Transaction t : txs) {
                    pw.println(
                            t.getOtherUser().getUsername() + "," +
                            t.getProductName() + "," +
                            t.getQuantity() + "," +
                            t.getOriginalPrice() + "," +
                            t.getDiscountedPrice()
                    );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ===========================
       APPEND SINGLE TRANSACTION
       =========================== */
    public static void appendTransaction(User user, Transaction t) {
        if (user == null || t == null) return;

        try {
            File dir = new File(TX_FOLDER);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(TX_FOLDER + user.getUsername() + "_transactions.txt");

            try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
                pw.println(
                        t.getOtherUser().getUsername() + "," +
                        t.getProductName() + "," +
                        t.getQuantity() + "," +
                        t.getOriginalPrice() + "," +
                        t.getDiscountedPrice()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ===========================
       LOAD TRANSACTIONS FOR USER
       =========================== */
    public static void loadTransactions(User user) {
        if (user == null) return;

        File file = new File(TX_FOLDER + user.getUsername() + "_transactions.txt");
        if (!file.exists()) {
            // nothing saved yet
            return;
        }

        List<Transaction> loaded = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String otherUsername = parts[0].trim();
                String productName   = parts[1].trim();

                int quantity;
                double originalPrice;
                double discountedPrice;
                try {
                    quantity        = Integer.parseInt(parts[2].trim());
                    originalPrice   = Double.parseDouble(parts[3].trim());
                    discountedPrice = Double.parseDouble(parts[4].trim());
                } catch (NumberFormatException ex) {
                    // skip malformed line
                    continue;
                }

                // Look up the "other" user from your user list
                User otherUser = UserManager.findByUsername(otherUsername);
                if (otherUser == null) {
                    // if for some reason user no longer exists, skip
                    continue;
                }

                Transaction tx = new Transaction(
                        otherUser,
                        productName,
                        quantity,
                        originalPrice,
                        discountedPrice
                );

                loaded.add(tx);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Put them into the in-memory log of this user
        if (user instanceof Buyer) {
            Buyer b = (Buyer) user;
            List<Transaction> log = b.getTransactionLog();
            log.clear();
            log.addAll(loaded);
        } else if (user instanceof Seller) {
            Seller s = (Seller) user;
            List<Transaction> log = s.getTransactionLog();
            log.clear();
            log.addAll(loaded);
        }
    }
}

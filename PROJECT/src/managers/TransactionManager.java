package managers;

import model.Product;
import model.User;

import java.io.*;
import java.util.List;

public class TransactionManager {

    private static final String TX_FOLDER = "src/storage/";

    public static void saveTransaction(User user, List<Product> products) {

        try {
            File dir = new File(TX_FOLDER);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(TX_FOLDER + user.getUsername() + "_transactions.txt");

            try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {

                pw.println("=== NEW TRANSACTION ===");

                for (Product p : products) {
                    pw.println(
                            p.getId() + "," +
                            p.getName() + "," +
                            p.getCategory() + "," +
                            p.getPrice() + "," +
                            p.getSeller().getUsername()
                    );
                }

                pw.println("======================");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package managers;

import model.Seller;
import model.Voucher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherManager {

    private static final String VOUCHER_FOLDER = "src/storage/";

    private static String getVoucherFileName(Seller seller) {
        return VOUCHER_FOLDER + seller.getUsername() + "_vouchers.txt";
    }

    /* ========== SAVE ========== */

    public static void saveVouchersToFile(Seller seller) {
        if (seller == null) return;

        File dir = new File(VOUCHER_FOLDER);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(getVoucherFileName(seller));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Voucher v : seller.getVoucherList()) {
                writer.write(v.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ========== LOAD ========== */

    public static void loadVouchersFromFile(Seller seller) {
        if (seller == null) return;

        File file = new File(getVoucherFileName(seller));
        List<Voucher> list = seller.getVoucherList();
        list.clear();

        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Voucher v = Voucher.fromFileString(line, seller);
                if (v != null) {
                    list.add(v);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /* ========== FIND ========== */

    public static Voucher findVoucherByName(Seller seller, String name) {
        if (seller == null || name == null) return null;

        for (Voucher v : seller.getVoucherList()) {
            if (v.getName().equalsIgnoreCase(name.trim())) {
                return v;
            }
        }
        return null;
    }

    public static List<Voucher> getVouchers(Seller seller) {
        if (seller == null) return new ArrayList<>();
        return seller.getVoucherList();
    }
}

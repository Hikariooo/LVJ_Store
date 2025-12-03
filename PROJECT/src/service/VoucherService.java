package service;

import managers.VoucherManager;
import model.Seller;
import model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherService {

    /* Seller creates a voucher */
    public static Voucher createVoucher(Seller seller,
                                        String name,
                                        double discountPercent,
                                        double maxDiscount) {
        if (seller == null || name == null || name.isBlank()) return null;

        Voucher voucher = new Voucher(name.trim(), seller, discountPercent, maxDiscount);
        seller.addVoucher(voucher);
        VoucherManager.saveVouchersToFile(seller);
        return voucher;
    }

    /* Seller removes a voucher */
    public static void removeVoucher(Seller seller, Voucher voucher) {
        if (seller == null || voucher == null) return;

        seller.removeVoucher(voucher);
        VoucherManager.saveVouchersToFile(seller);
    }

    /* Load + return all vouchers of a seller */
    public static List<Voucher> getSellerVouchers(Seller seller) {
        if (seller == null) return new ArrayList<>();
        VoucherManager.loadVouchersFromFile(seller);
        return seller.getVoucherList();
    }

    /* Find voucher by name (code) */
    public static Voucher findVoucher(Seller seller, String name) {
        if (seller == null || name == null) return null;
        VoucherManager.loadVouchersFromFile(seller);
        return VoucherManager.findVoucherByName(seller, name);
    }
}

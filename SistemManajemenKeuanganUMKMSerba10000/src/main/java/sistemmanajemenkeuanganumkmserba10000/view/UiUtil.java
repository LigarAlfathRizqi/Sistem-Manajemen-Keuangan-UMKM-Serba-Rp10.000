package sistemmanajemenkeuanganumkmserba10000.view;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.SQLException;

final class UiUtil {

    private UiUtil() {
    }

    static BigDecimal parseRupiah(String value) {
        String normalized = value.trim().replace("Rp", "").replace("rp", "")
                .replace(" ", "").replace(".", "").replace(',', '.');
        if (normalized.isEmpty()) {
            throw new NumberFormatException("Harga belum diisi.");
        }
        return new BigDecimal(normalized);
    }

    static void showError(Component parent, Exception exception) {
        String message = exception.getMessage();
        if (exception instanceof SQLException sqlException) {
            if (sqlException.getErrorCode() == 1062) {
                message = "Nama tersebut sudah digunakan. Gunakan nama lain.";
            } else if (sqlException.getErrorCode() == 1451 || sqlException.getErrorCode() == 1452) {
                message = "Data ini masih dipakai oleh produk atau transaksi lain.";
            } else if (sqlException.getErrorCode() == 1146 || sqlException.getErrorCode() == 1054) {
                message = "Struktur database belum sesuai. Jalankan database/schema.sql atau migration.sql.";
            } else if (sqlException.getErrorCode() == 1049) {
                message = "Database umkm_serba_10000 belum dibuat. Jalankan database/schema.sql.";
            } else if (sqlException.getErrorCode() == 1045 || sqlException.getErrorCode() == 0) {
                message = "Tidak bisa terhubung ke MySQL. Periksa server, database, dan variabel UMKM_DB_USER/UMKM_DB_PASSWORD.";
            }
        }
        if (message == null || message.isBlank()) {
            message = exception.getClass().getSimpleName();
        }
        JOptionPane.showMessageDialog(parent, message, "Operasi gagal", JOptionPane.ERROR_MESSAGE);
    }
}

package sistemmanajemenkeuanganumkmserba10000.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class Navigasi {

    private Navigasi() {
    }

    static void pasang(JFrame frame) {
        pasangPada(frame, frame.getContentPane());
    }

    private static void pasangPada(JFrame frame, Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel label) {
                String text = label.getText();
                if (text != null && isMenu(text.trim())) {
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    label.setToolTipText("Buka " + text.trim());
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent event) {
                            buka(frame, text.trim());
                        }
                    });
                }
            }
            if (component instanceof Container child) {
                pasangPada(frame, child);
            }
        }
    }

    private static boolean isMenu(String text) {
        return text.equalsIgnoreCase("Dashboard")
                || text.equalsIgnoreCase("Transaksi")
                || text.equalsIgnoreCase("Produk")
                || text.equalsIgnoreCase("Bahan")
                || text.equalsIgnoreCase("Bahan/Barang")
                || text.equalsIgnoreCase("Laporan");
    }

    private static void buka(JFrame current, String menu) {
        if (menu.equalsIgnoreCase("Dashboard") && !(current instanceof DashboardFrame)) {
            new DashboardFrame().setVisible(true);
        } else if (menu.equalsIgnoreCase("Transaksi") && !(current instanceof Transaksi)) {
            new Transaksi().setVisible(true);
        } else if (menu.equalsIgnoreCase("Produk") && !(current instanceof ProdukFrame)) {
            new ProdukFrame().setVisible(true);
        } else if ((menu.equalsIgnoreCase("Bahan") || menu.equalsIgnoreCase("Bahan/Barang"))
                && !(current instanceof bahanBarang)) {
            new bahanBarang().setVisible(true);
        } else if (menu.equalsIgnoreCase("Laporan") && !(current instanceof Laporan)) {
            new Laporan().setVisible(true);
        } else {
            return;
        }
        current.dispose();
    }
}

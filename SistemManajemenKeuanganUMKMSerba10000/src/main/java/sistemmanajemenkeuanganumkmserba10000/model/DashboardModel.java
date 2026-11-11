
package sistemmanajemenkeuanganumkmserba10000.model;

import java.math.BigDecimal;

public class DashboardModel {

    private final BigDecimal totalPemasukan;
    private final BigDecimal totalPengeluaran;
    private final BigDecimal totalLaba;

    public DashboardModel(
            BigDecimal totalPemasukan,
            BigDecimal totalPengeluaran,
            BigDecimal totalLaba) {

        this.totalPemasukan = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.totalLaba = totalLaba;
    }

    public static DashboardModel dariLaporan(
            LaporanTableModel laporan) {

        return new DashboardModel(
                laporan.getTotalPemasukan(),
                laporan.getTotalPengeluaran(),
                laporan.getTotalLaba()
        );
    }

    public BigDecimal getTotalPemasukan() {
        return totalPemasukan;
    }

    public BigDecimal getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public BigDecimal getTotalLaba() {
        return totalLaba;
    }
}

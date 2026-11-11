
package sistemmanajemenkeuanganumkmserba10000.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class RupiahUtil {

    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    private RupiahUtil() {
    }

    public static String format(BigDecimal nominal) {
        if (nominal == null) {
            nominal = BigDecimal.ZERO;
        }

        return FORMAT.format(nominal);
    }
}

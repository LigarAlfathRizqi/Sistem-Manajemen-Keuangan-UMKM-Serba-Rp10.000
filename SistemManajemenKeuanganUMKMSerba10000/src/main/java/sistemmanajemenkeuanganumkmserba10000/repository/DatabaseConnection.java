
package sistemmanajemenkeuanganumkmserba10000.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            System.getenv().getOrDefault("UMKM_DB_URL",
                    "jdbc:mysql://localhost:3306/umkm_serba_10000?serverTimezone=Asia/Jakarta&useSSL=false&allowPublicKeyRetrieval=true");

    private static final String USER = System.getenv().getOrDefault("UMKM_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("UMKM_DB_PASSWORD", "");

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}

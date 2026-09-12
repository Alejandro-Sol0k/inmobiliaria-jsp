package co.edu.uts.inmobiliaria.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class Database {
    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        if (isRemoteConfigured()) return getRemoteConnection();
        return getLocalConnection();
    }

    public static Connection getRemoteConnection() throws SQLException {
        String onlineUrl = setting("INMOBILIARIA_DB_URL", "inmobiliaria.db.url");
        if (onlineUrl.isEmpty()) throw new SQLException("No hay una conexión remota configurada.");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("No se encontró el controlador JDBC de MySQL.", exception);
        }
        return DriverManager.getConnection(onlineUrl,
                setting("INMOBILIARIA_DB_USER", "inmobiliaria.db.user"),
                setting("INMOBILIARIA_DB_PASSWORD", "inmobiliaria.db.password"));
    }

    public static Connection getLocalConnection() throws SQLException {
        try {
            DataSource dataSource = (DataSource) new InitialContext()
                    .lookup("java:comp/env/jdbc/inmobiliaria");
            return dataSource.getConnection();
        } catch (NamingException exception) {
            throw new SQLException("No se encontro el recurso JDBC jdbc/inmobiliaria.", exception);
        }
    }

    public static boolean isRemoteConfigured() {
        return !setting("INMOBILIARIA_DB_URL", "inmobiliaria.db.url").isEmpty();
    }

    public static boolean isLocalSyncEnabled() {
        return "true".equalsIgnoreCase(setting("INMOBILIARIA_SYNC_LOCAL", "inmobiliaria.sync.local"));
    }

    public static int getSyncIntervalSeconds() {
        String configured = setting("INMOBILIARIA_SYNC_INTERVAL_SECONDS", "inmobiliaria.sync.interval.seconds");
        if (configured.isEmpty()) return 10;
        try {
            return Math.max(10, Math.min(3600, Integer.parseInt(configured)));
        } catch (NumberFormatException exception) {
            return 10;
        }
    }

    private static String setting(String environmentName, String propertyName) {
        String property = System.getProperty(propertyName);
        if (property != null && !property.trim().isEmpty()) {
            return property.trim();
        }
        String environment = System.getenv(environmentName);
        return environment == null ? "" : environment.trim();
    }
}

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
        String onlineUrl = setting("INMOBILIARIA_DB_URL", "inmobiliaria.db.url");
        if (!onlineUrl.isEmpty()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException exception) {
                throw new SQLException("No se encontró el controlador JDBC de MySQL.", exception);
            }
            return DriverManager.getConnection(onlineUrl,
                    setting("INMOBILIARIA_DB_USER", "inmobiliaria.db.user"),
                    setting("INMOBILIARIA_DB_PASSWORD", "inmobiliaria.db.password"));
        }
        try {
            DataSource dataSource = (DataSource) new InitialContext()
                    .lookup("java:comp/env/jdbc/inmobiliaria");
            return dataSource.getConnection();
        } catch (NamingException exception) {
            throw new SQLException("No se encontro el recurso JDBC jdbc/inmobiliaria.", exception);
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

package co.edu.uts.inmobiliaria.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class Database {
    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            DataSource dataSource = (DataSource) new InitialContext()
                    .lookup("java:comp/env/jdbc/inmobiliaria");
            return dataSource.getConnection();
        } catch (NamingException exception) {
            throw new SQLException("No se encontro el recurso JDBC jdbc/inmobiliaria.", exception);
        }
    }
}

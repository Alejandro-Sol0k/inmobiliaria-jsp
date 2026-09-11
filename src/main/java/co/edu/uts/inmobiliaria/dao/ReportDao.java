package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.ReportRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ReportDao {
    public List<ReportRow> activePropertiesByCity() throws SQLException {
        return grouped("SELECT c.nombre AS etiqueta, COUNT(*) AS total FROM propiedad p "
                + "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad WHERE p.disponible = TRUE "
                + "GROUP BY c.id_ciudad, c.nombre ORDER BY total DESC, c.nombre", "etiqueta");
    }

    public List<ReportRow> requestsByStatus() throws SQLException {
        return grouped("SELECT estado AS etiqueta, COUNT(*) AS total FROM solicitud GROUP BY estado ORDER BY total DESC", "etiqueta");
    }

    public List<ReportRow> appointmentsByStatus() throws SQLException {
        return grouped("SELECT estado AS etiqueta, COUNT(*) AS total FROM cita GROUP BY estado ORDER BY total DESC", "etiqueta");
    }

    public List<ReportRow> propertiesByOperation() throws SQLException {
        return grouped("SELECT operacion AS etiqueta, COUNT(*) AS total FROM propiedad WHERE disponible = TRUE "
                + "GROUP BY operacion ORDER BY total DESC", "etiqueta");
    }

    private List<ReportRow> grouped(String sql, String labelColumn) throws SQLException {
        List<ReportRow> rows = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                rows.add(new ReportRow(result.getString(labelColumn), result.getInt("total")));
            }
        }
        return rows;
    }
}

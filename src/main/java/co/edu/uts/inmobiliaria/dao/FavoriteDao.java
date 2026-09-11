package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.Property;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class FavoriteDao {
    public List<Property> findByUserId(int userId) throws SQLException {
        String sql = "SELECT p.id_propiedad, p.titulo, c.nombre AS ciudad, tp.nombre AS tipo, p.operacion, "
                + "p.precio, p.habitaciones, p.banos, p.area_m2, p.matricula_inmobiliaria, p.descripcion, p.direccion, "
                + "(SELECT ip.url FROM imagen_propiedad ip WHERE ip.id_propiedad = p.id_propiedad "
                + "ORDER BY ip.es_principal DESC, ip.id_imagen LIMIT 1) AS imagen "
                + "FROM favorito f INNER JOIN propiedad p ON p.id_propiedad = f.id_propiedad "
                + "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad INNER JOIN tipo_propiedad tp ON tp.id_tipo = p.id_tipo "
                + "WHERE f.id_cliente = ? AND p.disponible = TRUE ORDER BY f.creado_en DESC";
        List<Property> properties = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    properties.add(new Property(result.getInt("id_propiedad"), result.getString("titulo"),
                            result.getString("ciudad"), result.getString("tipo"), result.getString("operacion"),
                            result.getBigDecimal("precio"), result.getInt("habitaciones"), result.getInt("banos"),
                            result.getBigDecimal("area_m2"), result.getString("imagen"),
                            result.getString("matricula_inmobiliaria"), result.getString("descripcion"),
                            result.getString("direccion")));
                }
            }
        }
        return properties;
    }

    public void add(int userId, int propertyId) throws SQLException {
        String sql = "INSERT IGNORE INTO favorito (id_cliente, id_propiedad) "
                + "SELECT ?, id_propiedad FROM propiedad WHERE id_propiedad = ? AND disponible = TRUE";
        execute(sql, userId, propertyId);
    }

    public void remove(int userId, int propertyId) throws SQLException {
        String sql = "DELETE FROM favorito WHERE id_cliente = ? AND id_propiedad = ?";
        execute(sql, userId, propertyId);
    }

    private void execute(String sql, int userId, int propertyId) throws SQLException {
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, propertyId);
            statement.executeUpdate();
        }
    }
}

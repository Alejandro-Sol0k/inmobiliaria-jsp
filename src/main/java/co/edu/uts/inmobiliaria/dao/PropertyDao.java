package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.Property;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class PropertyDao {
    public List<Property> findPublic(String city, String type, String operation,
            BigDecimal maxPrice) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id_propiedad, p.titulo, c.nombre AS ciudad, tp.nombre AS tipo, "
                + "p.operacion, p.precio, p.habitaciones, p.banos, p.area_m2, "
                + "(SELECT ip.url FROM imagen_propiedad ip WHERE ip.id_propiedad = p.id_propiedad "
                + "ORDER BY ip.es_principal DESC, ip.id_imagen LIMIT 1) AS imagen "
                + "FROM propiedad p INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad "
                + "INNER JOIN tipo_propiedad tp ON tp.id_tipo = p.id_tipo "
                + "WHERE p.disponible = TRUE");
        List<Object> parameters = new ArrayList<>();
        if (!city.isEmpty()) {
            sql.append(" AND LOWER(c.nombre) LIKE ?");
            parameters.add("%" + city.toLowerCase() + "%");
        }
        if (!type.isEmpty()) {
            sql.append(" AND tp.nombre = ?");
            parameters.add(type);
        }
        if (!operation.isEmpty()) {
            sql.append(" AND p.operacion = ?");
            parameters.add(operation);
        }
        if (maxPrice != null) {
            sql.append(" AND p.precio <= ?");
            parameters.add(maxPrice);
        }
        sql.append(" ORDER BY p.creado_en DESC, p.id_propiedad DESC");

        List<Property> properties = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    properties.add(new Property(result.getInt("id_propiedad"),
                            result.getString("titulo"), result.getString("ciudad"),
                            result.getString("tipo"), result.getString("operacion"),
                            result.getBigDecimal("precio"), result.getInt("habitaciones"),
                            result.getInt("banos"), result.getBigDecimal("area_m2"),
                            result.getString("imagen")));
                }
            }
        }
        return properties;
    }
}

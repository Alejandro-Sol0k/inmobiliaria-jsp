package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.Property;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public void create(String title, String city, String type, String operation,
            String registration, String address, String description, BigDecimal price,
            int bedrooms, int bathrooms, BigDecimal area, String imageUrl,
            List<String> featureNames) throws SQLException {
        String idCompanySql = "SELECT id_inmobiliaria FROM inmobiliaria ORDER BY id_inmobiliaria LIMIT 1";
        String idCitySql = "SELECT id_ciudad FROM ciudad WHERE nombre = ?";
        String idTypeSql = "SELECT id_tipo FROM tipo_propiedad WHERE nombre = ?";
        String insertSql = "INSERT INTO propiedad (id_inmobiliaria, id_ciudad, id_tipo, matricula_inmobiliaria, titulo, descripcion, direccion, precio, operacion, habitaciones, banos, area_m2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int companyId = findId(connection, idCompanySql, null, "No existe una inmobiliaria configurada.");
                int cityId = findId(connection, idCitySql, city, "La ciudad seleccionada no existe.");
                int typeId = findId(connection, idTypeSql, type, "El tipo de propiedad seleccionado no existe.");
                int propertyId;
                try (PreparedStatement statement = connection.prepareStatement(insertSql,
                        Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, companyId);
                    statement.setInt(2, cityId);
                    statement.setInt(3, typeId);
                    statement.setString(4, registration);
                    statement.setString(5, title);
                    statement.setString(6, description);
                    statement.setString(7, address);
                    statement.setBigDecimal(8, price);
                    statement.setString(9, operation);
                    statement.setInt(10, bedrooms);
                    statement.setInt(11, bathrooms);
                    statement.setBigDecimal(12, area);
                    statement.executeUpdate();
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("No fue posible obtener la propiedad creada.");
                        }
                        propertyId = generatedKeys.getInt(1);
                    }
                }

                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal) VALUES (?, ?, ?, TRUE)")) {
                        statement.setInt(1, propertyId);
                        statement.setString(2, imageUrl.trim());
                        statement.setString(3, title);
                        statement.executeUpdate();
                    }
                }

                if (featureNames != null && !featureNames.isEmpty()) {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "INSERT IGNORE INTO propiedad_caracteristica (id_propiedad, id_caracteristica) "
                            + "SELECT ?, id_caracteristica FROM caracteristica WHERE nombre = ?")) {
                        for (String featureName : featureNames) {
                            if (featureName != null && !featureName.trim().isEmpty()) {
                                statement.setInt(1, propertyId);
                                statement.setString(2, featureName.trim());
                                statement.addBatch();
                            }
                        }
                        statement.executeBatch();
                    }
                }
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    public void deactivate(int propertyId) throws SQLException {
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE propiedad SET disponible = FALSE WHERE id_propiedad = ?")) {
            statement.setInt(1, propertyId);
            statement.executeUpdate();
        }
    }

    private int findId(Connection connection, String sql, String parameter, String message)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (parameter != null) {
                statement.setString(1, parameter);
            }
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt(1);
                }
            }
        }
        throw new SQLException(message);
    }
}

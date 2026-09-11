package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.Profile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProfileDao {
    public Profile findByUserId(int userId) throws SQLException {
        String sql = "SELECT id_perfil, id_usuario, nombres, apellidos, documento, telefono, direccion, foto_url "
                + "FROM perfil WHERE id_usuario = ?";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return map(result);
                }
            }
        }
        return null;
    }

    public void update(int userId, String names, String lastNames, String document,
            String phone, String address, String photoUrl) throws SQLException {
        String sql = "UPDATE perfil SET nombres = ?, apellidos = ?, documento = ?, telefono = ?, "
                + "direccion = ?, foto_url = ? WHERE id_usuario = ?";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, names);
            statement.setString(2, lastNames);
            statement.setString(3, document);
            statement.setString(4, nullable(phone));
            statement.setString(5, nullable(address));
            statement.setString(6, nullable(photoUrl));
            statement.setInt(7, userId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("No se encontró el perfil del usuario.");
            }
        }
    }

    private static Profile map(ResultSet result) throws SQLException {
        return new Profile(result.getInt("id_perfil"), result.getInt("id_usuario"),
                result.getString("nombres"), result.getString("apellidos"),
                result.getString("documento"), result.getString("telefono"),
                result.getString("direccion"), result.getString("foto_url"));
    }

    private static String nullable(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}

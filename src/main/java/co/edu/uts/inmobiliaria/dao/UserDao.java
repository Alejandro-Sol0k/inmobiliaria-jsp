package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.AuthenticatedUser;
import co.edu.uts.inmobiliaria.security.PasswordUtil;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDao {
    public AuthenticatedUser authenticate(String email, String password)
            throws SQLException, GeneralSecurityException {
        String sql = "SELECT u.id_usuario, u.correo, u.password_hash, u.password_salt, "
                + "COALESCE(r.nombre, 'CLIENTE') AS rol "
                + "FROM usuario u LEFT JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario "
                + "LEFT JOIN rol r ON r.id_rol = ur.id_rol "
                + "WHERE u.correo = ? AND u.activo = TRUE "
                + "ORDER BY FIELD(r.nombre, 'ADMINISTRADOR', 'INMOBILIARIA', 'CLIENTE', 'VISITANTE') LIMIT 1";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next() || !PasswordUtil.matches(password,
                        result.getString("password_salt"), result.getString("password_hash"))) {
                    return null;
                }
                return new AuthenticatedUser(result.getInt("id_usuario"),
                        result.getString("correo"), result.getString("rol"));
            }
        }
    }

    public void registerClient(String email, String password, String names,
            String lastNames, String document)
            throws SQLException, GeneralSecurityException {
        String insertUser = "INSERT INTO usuario (correo, password_hash, password_salt) VALUES (?, ?, ?)";
        String insertProfile = "INSERT INTO perfil (id_usuario, nombres, apellidos, documento) VALUES (?, ?, ?, ?)";
        String assignRole = "INSERT INTO usuario_rol (id_usuario, id_rol) "
                + "SELECT ?, id_rol FROM rol WHERE nombre = 'CLIENTE'";
        String salt = PasswordUtil.newSalt();
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement user = connection.prepareStatement(insertUser,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                user.setString(1, email);
                user.setString(2, PasswordUtil.hash(password, salt));
                user.setString(3, salt);
                user.executeUpdate();
                try (ResultSet keys = user.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se pudo obtener el usuario creado.");
                    }
                    int userId = keys.getInt(1);
                    try (PreparedStatement profile = connection.prepareStatement(insertProfile);
                            PreparedStatement role = connection.prepareStatement(assignRole)) {
                        profile.setInt(1, userId);
                        profile.setString(2, names);
                        profile.setString(3, lastNames);
                        profile.setString(4, document);
                        profile.executeUpdate();
                        role.setInt(1, userId);
                        role.executeUpdate();
                    }
                }
            } catch (SQLException | GeneralSecurityException exception) {
                connection.rollback();
                throw exception;
            }
            connection.commit();
        }
    }
}

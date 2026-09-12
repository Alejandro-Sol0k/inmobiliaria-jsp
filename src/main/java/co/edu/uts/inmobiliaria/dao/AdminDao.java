package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.AdminUser;
import co.edu.uts.inmobiliaria.model.CatalogItem;
import co.edu.uts.inmobiliaria.security.PasswordUtil;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class AdminDao {
    public int createUser(String email, String password, String names, String lastNames,
            String document, String roleName) throws SQLException, GeneralSecurityException {
        String insertUser = "INSERT INTO usuario (correo, password_hash, password_salt) VALUES (?, ?, ?)";
        String insertProfile = "INSERT INTO perfil (id_usuario, nombres, apellidos, documento) VALUES (?, ?, ?, ?)";
        String assignRole = "INSERT INTO usuario_rol (id_usuario, id_rol) SELECT ?, id_rol FROM rol WHERE nombre = ?";
        String salt = PasswordUtil.newSalt();
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement user = connection.prepareStatement(insertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                user.setString(1, email);
                user.setString(2, PasswordUtil.hash(password, salt));
                user.setString(3, salt);
                user.executeUpdate();
                try (ResultSet keys = user.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No se pudo obtener el usuario creado.");
                    int userId = keys.getInt(1);
                    try (PreparedStatement profile = connection.prepareStatement(insertProfile);
                            PreparedStatement role = connection.prepareStatement(assignRole)) {
                        profile.setInt(1, userId);
                        profile.setString(2, names);
                        profile.setString(3, lastNames);
                        profile.setString(4, document);
                        profile.executeUpdate();
                        role.setInt(1, userId);
                        role.setString(2, roleName);
                        if (role.executeUpdate() == 0) throw new SQLException("El rol seleccionado no existe.");
                    }
                    connection.commit();
                    return userId;
                }
            } catch (SQLException | GeneralSecurityException exception) {
                connection.rollback();
                if (exception instanceof SQLException && ((SQLException) exception).getErrorCode() == 1062) {
                    throw new SQLException("El correo o documento ya está registrado.", exception);
                }
                throw exception;
            }
        }
    }

    public List<AdminUser> findUsers() throws SQLException {
        String sql = "SELECT u.id_usuario, u.correo, u.activo, "
                + "COALESCE(NULLIF(CONCAT_WS(' ', p.nombres, p.apellidos), ''), 'Sin perfil') AS nombre, "
                + "COALESCE(GROUP_CONCAT(r.nombre ORDER BY r.nombre SEPARATOR ', '), 'SIN_ROL') AS roles "
                + "FROM usuario u LEFT JOIN perfil p ON p.id_usuario = u.id_usuario "
                + "LEFT JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario LEFT JOIN rol r ON r.id_rol = ur.id_rol "
                + "GROUP BY u.id_usuario, u.correo, u.activo, p.nombres, p.apellidos ORDER BY u.creado_en DESC";
        List<AdminUser> users = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                users.add(new AdminUser(result.getInt("id_usuario"), result.getString("correo"),
                        result.getString("nombre"), result.getBoolean("activo"), result.getString("roles")));
            }
        }
        return users;
    }

    public void setRole(int userId, String roleName) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement delete = connection.prepareStatement("DELETE FROM usuario_rol WHERE id_usuario = ?")) {
                    delete.setInt(1, userId);
                    delete.executeUpdate();
                }
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO usuario_rol (id_usuario, id_rol) SELECT ?, id_rol FROM rol WHERE nombre = ?")) {
                    insert.setInt(1, userId);
                    insert.setString(2, roleName);
                    if (insert.executeUpdate() == 0) throw new SQLException("El rol seleccionado no existe.");
                }
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    public void toggleUser(int userId, int adminId) throws SQLException {
        if (userId == adminId) throw new SQLException("No puedes desactivar tu propia cuenta.");
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE usuario SET activo = NOT activo WHERE id_usuario = ?")) {
            statement.setInt(1, userId);
            if (statement.executeUpdate() == 0) throw new SQLException("No se encontró el usuario.");
        }
    }

    public List<CatalogItem> findCatalog(String catalog) throws SQLException {
        String table = tableFor(catalog);
        String idColumn = "ciudad".equals(catalog) ? "id_ciudad"
                : "tipo".equals(catalog) ? "id_tipo" : "id_caracteristica";
        List<CatalogItem> items = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT " + idColumn + ", nombre FROM " + table + " ORDER BY nombre");
                ResultSet result = statement.executeQuery()) {
            while (result.next()) items.add(new CatalogItem(result.getInt(1), result.getString("nombre")));
        }
        return items;
    }

    public void addCatalog(String catalog, String name) throws SQLException {
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + tableFor(catalog) + " (nombre) VALUES (?)")) {
            statement.setString(1, name.trim());
            statement.executeUpdate();
        }
    }

    private static String tableFor(String catalog) throws SQLException {
        if ("ciudad".equals(catalog)) return "ciudad";
        if ("tipo".equals(catalog)) return "tipo_propiedad";
        if ("caracteristica".equals(catalog)) return "caracteristica";
        throw new SQLException("Catálogo no válido.");
    }
}

package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.AuditEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Registra acciones importantes sin interrumpir la operación principal si la auditoría falla. */
public final class AuditDao {
    private static final String INSERT = "INSERT INTO auditoria "
            + "(id_usuario, accion, entidad, id_entidad, detalle) VALUES (?, ?, ?, ?, ?)";

    public void log(Integer userId, String action, String entity, String entityId, String detail) {
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (userId == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, userId);
            }
            statement.setString(2, truncate(action, 80));
            statement.setString(3, truncate(entity, 80));
            statement.setString(4, truncate(entityId, 40));
            statement.setString(5, truncate(detail, 255));
            statement.executeUpdate();
        } catch (SQLException ignored) {
            // La auditoría no debe impedir que el usuario complete una operación válida.
        }
    }

    public List<AuditEntry> findRecent(int limit) throws SQLException {
        List<AuditEntry> entries = new ArrayList<>();
        String sql = "SELECT COALESCE(NULLIF(CONCAT_WS(' ', p.nombres, p.apellidos), ''), u.correo, 'Sistema') AS actor, "
                + "a.accion, a.entidad, a.id_entidad, a.detalle, a.creado_en "
                + "FROM auditoria a LEFT JOIN usuario u ON u.id_usuario = a.id_usuario "
                + "LEFT JOIN perfil p ON p.id_usuario = a.id_usuario "
                + "ORDER BY a.creado_en DESC, a.id_auditoria DESC LIMIT ?";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.max(1, Math.min(limit, 100)));
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    entries.add(new AuditEntry(result.getString("actor"), result.getString("accion"),
                            result.getString("entidad"), result.getString("id_entidad"),
                            result.getString("detalle"), result.getTimestamp("creado_en")));
                }
            }
        }
        return entries;
    }

    private static String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String clean = value.trim();
        return clean.length() <= maxLength ? clean : clean.substring(0, maxLength);
    }
}

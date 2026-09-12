package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.ChatConversation;
import co.edu.uts.inmobiliaria.model.ChatMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class ChatDao {
    public List<ChatConversation> findConversations(int userId, String role) throws SQLException {
        String filter = "ADMINISTRADOR".equals(role) ? "" : "CLIENTE".equals(role)
                ? "WHERE c.id_cliente = ?" : "WHERE c.id_inmobiliaria = ?";
        String sql = "SELECT c.id_chat, COALESCE(NULLIF(CONCAT_WS(' ', cp.nombres, cp.apellidos), ''), cu.correo) AS cliente, "
                + "COALESCE(NULLIF(CONCAT_WS(' ', ap.nombres, ap.apellidos), ''), au.correo) AS inmobiliaria, "
                + "p.titulo, c.asunto, c.estado, c.creado_en, c.cerrado_en "
                + "FROM chat_conversacion c INNER JOIN usuario cu ON cu.id_usuario = c.id_cliente "
                + "LEFT JOIN perfil cp ON cp.id_usuario = cu.id_usuario "
                + "INNER JOIN usuario au ON au.id_usuario = c.id_inmobiliaria "
                + "LEFT JOIN perfil ap ON ap.id_usuario = au.id_usuario "
                + "LEFT JOIN propiedad p ON p.id_propiedad = c.id_propiedad "
                + filter + " ORDER BY c.estado ASC, c.creado_en DESC, c.id_chat DESC";
        List<ChatConversation> conversations = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!filter.isEmpty()) statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) conversations.add(readConversation(result));
            }
        }
        return conversations;
    }

    public ChatConversation findConversation(int chatId, int userId, String role) throws SQLException {
        String filter = "ADMINISTRADOR".equals(role) ? "" : "CLIENTE".equals(role)
                ? " AND c.id_cliente = ?" : " AND c.id_inmobiliaria = ?";
        String sql = "SELECT c.id_chat, COALESCE(NULLIF(CONCAT_WS(' ', cp.nombres, cp.apellidos), ''), cu.correo) AS cliente, "
                + "COALESCE(NULLIF(CONCAT_WS(' ', ap.nombres, ap.apellidos), ''), au.correo) AS inmobiliaria, "
                + "p.titulo, c.asunto, c.estado, c.creado_en, c.cerrado_en "
                + "FROM chat_conversacion c INNER JOIN usuario cu ON cu.id_usuario = c.id_cliente "
                + "LEFT JOIN perfil cp ON cp.id_usuario = cu.id_usuario "
                + "INNER JOIN usuario au ON au.id_usuario = c.id_inmobiliaria "
                + "LEFT JOIN perfil ap ON ap.id_usuario = au.id_usuario "
                + "LEFT JOIN propiedad p ON p.id_propiedad = c.id_propiedad "
                + "WHERE c.id_chat = ?" + filter;
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            if (!filter.isEmpty()) statement.setInt(2, userId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readConversation(result) : null;
            }
        }
    }

    public List<ChatMessage> findMessages(int chatId, int currentUserId) throws SQLException {
        String sql = "SELECT COALESCE(NULLIF(CONCAT_WS(' ', p.nombres, p.apellidos), ''), u.correo) AS remitente, "
                + "m.id_remitente, m.mensaje, m.enviado_en FROM chat_mensaje m "
                + "INNER JOIN usuario u ON u.id_usuario = m.id_remitente LEFT JOIN perfil p ON p.id_usuario = u.id_usuario "
                + "WHERE m.id_chat = ? ORDER BY m.enviado_en ASC, m.id_mensaje ASC";
        List<ChatMessage> messages = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) messages.add(new ChatMessage(result.getString("remitente"),
                        result.getInt("id_remitente") == currentUserId, result.getString("mensaje"),
                        result.getTimestamp("enviado_en")));
            }
        }
        return messages;
    }

    public int createConversation(int clientId, int propertyId, String subject, String message) throws SQLException {
        String agencySql = "SELECT u.id_usuario FROM usuario u INNER JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario "
                + "INNER JOIN rol r ON r.id_rol = ur.id_rol WHERE r.nombre = 'INMOBILIARIA' AND u.activo = TRUE "
                + "ORDER BY u.id_usuario LIMIT 1";
        String chatSql = "INSERT INTO chat_conversacion (id_cliente, id_inmobiliaria, id_propiedad, asunto) VALUES (?, ?, NULLIF(?, 0), ?)";
        String messageSql = "INSERT INTO chat_mensaje (id_chat, id_remitente, mensaje) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int agencyId;
                try (PreparedStatement agency = connection.prepareStatement(agencySql); ResultSet result = agency.executeQuery()) {
                    if (!result.next()) throw new SQLException("No hay un usuario inmobiliario activo para atender el chat.");
                    agencyId = result.getInt(1);
                }
                int chatId;
                try (PreparedStatement chat = connection.prepareStatement(chatSql, Statement.RETURN_GENERATED_KEYS)) {
                    chat.setInt(1, clientId);
                    chat.setInt(2, agencyId);
                    chat.setInt(3, propertyId);
                    chat.setString(4, subject);
                    chat.executeUpdate();
                    try (ResultSet keys = chat.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("No fue posible crear la conversación.");
                        chatId = keys.getInt(1);
                    }
                }
                insertMessage(connection, messageSql, chatId, clientId, message);
                connection.commit();
                return chatId;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    public void addMessage(int chatId, int userId, String message, String role) throws SQLException {
        String participant = "ADMINISTRADOR".equals(role) ? "" : " AND (id_cliente = ? OR id_inmobiliaria = ?)";
        String sql = "INSERT INTO chat_mensaje (id_chat, id_remitente, mensaje) SELECT id_chat, ?, ? "
                + "FROM chat_conversacion WHERE id_chat = ? AND estado = 'ABIERTA'" + participant;
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.setInt(3, chatId);
            if (!participant.isEmpty()) {
                statement.setInt(4, userId);
                statement.setInt(5, userId);
            }
            if (statement.executeUpdate() == 0) throw new SQLException("La conversación no existe, está cerrada o no te pertenece.");
        }
    }

    public void closeConversation(int chatId, int userId, String role) throws SQLException {
        String where = "ADMINISTRADOR".equals(role) ? "id_chat = ?" : "id_chat = ? AND id_inmobiliaria = ?";
        String sql = "UPDATE chat_conversacion SET estado = 'CERRADA', cerrado_en = CURRENT_TIMESTAMP WHERE " + where + " AND estado = 'ABIERTA'";
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            if (!"ADMINISTRADOR".equals(role)) statement.setInt(2, userId);
            if (statement.executeUpdate() == 0) throw new SQLException("El chat no existe, ya está cerrado o no puedes cerrarlo.");
        }
    }

    private void insertMessage(Connection connection, String sql, int chatId, int userId, String message) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            statement.setInt(2, userId);
            statement.setString(3, message);
            statement.executeUpdate();
        }
    }

    private ChatConversation readConversation(ResultSet result) throws SQLException {
        return new ChatConversation(result.getInt("id_chat"), result.getString("cliente"),
                result.getString("inmobiliaria"), result.getString("titulo"), result.getString("asunto"),
                result.getString("estado"), result.getTimestamp("creado_en"), result.getTimestamp("cerrado_en"));
    }
}

package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.RequestDocument;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DocumentDao {
    public List<RequestDocument> findDocuments(int userId, boolean manager) throws SQLException {
        String sql = "SELECT d.id_documento, d.id_solicitud, p.titulo, d.nombre_archivo, d.url_archivo, d.estado "
                + "FROM documento_solicitud d INNER JOIN solicitud s ON s.id_solicitud = d.id_solicitud "
                + "INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad "
                + (manager ? "" : "WHERE s.id_cliente = ? ") + "ORDER BY d.id_documento DESC";
        List<RequestDocument> documents = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!manager) {
                statement.setInt(1, userId);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    documents.add(new RequestDocument(result.getInt("id_documento"),
                            result.getInt("id_solicitud"), result.getString("titulo"),
                            result.getString("nombre_archivo"), result.getString("url_archivo"),
                            result.getString("estado")));
                }
            }
        }
        return documents;
    }

    public void create(int userId, int requestId, String fileName, String fileUrl) throws SQLException {
        String sql = "INSERT INTO documento_solicitud (id_solicitud, nombre_archivo, url_archivo) "
                + "SELECT id_solicitud, ?, ? FROM solicitud WHERE id_solicitud = ? AND id_cliente = ?";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fileName);
            statement.setString(2, fileUrl);
            statement.setInt(3, requestId);
            statement.setInt(4, userId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("La solicitud no existe o no pertenece al usuario.");
            }
        }
    }

    public void updateStatus(int documentId, String status) throws SQLException {
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE documento_solicitud SET estado = ? WHERE id_documento = ?")) {
            statement.setString(1, status);
            statement.setInt(2, documentId);
            statement.executeUpdate();
        }
    }
}

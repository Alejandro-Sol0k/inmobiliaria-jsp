package co.edu.uts.inmobiliaria.dao;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.model.Appointment;
import co.edu.uts.inmobiliaria.model.PropertyRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public final class OperationDao {
    public List<Appointment> findAppointments(int userId, boolean manager) throws SQLException {
        String sql = "SELECT c.id_cita, c.id_propiedad, p.titulo, c.fecha_hora, c.estado, c.observaciones, "
                + "COALESCE(NULLIF(CONCAT_WS(' ', pf.nombres, pf.apellidos), ''), u.correo) AS cliente "
                + "FROM cita c INNER JOIN propiedad p ON p.id_propiedad = c.id_propiedad "
                + "INNER JOIN usuario u ON u.id_usuario = c.id_cliente "
                + "LEFT JOIN perfil pf ON pf.id_usuario = u.id_usuario "
                + (manager ? "" : "WHERE c.id_cliente = ? ")
                + "ORDER BY c.fecha_hora DESC, c.id_cita DESC";
        List<Appointment> appointments = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!manager) {
                statement.setInt(1, userId);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    appointments.add(new Appointment(result.getInt("id_cita"), result.getInt("id_propiedad"),
                            result.getString("titulo"), result.getString("cliente"),
                            result.getTimestamp("fecha_hora"), result.getString("estado"),
                            result.getString("observaciones")));
                }
            }
        }
        return appointments;
    }

    public List<PropertyRequest> findRequests(int userId, boolean manager) throws SQLException {
        String sql = "SELECT s.id_solicitud, s.id_propiedad, p.titulo, s.tipo_operacion, s.estado, s.creada_en, "
                + "COALESCE(NULLIF(CONCAT_WS(' ', pf.nombres, pf.apellidos), ''), u.correo) AS cliente "
                + "FROM solicitud s INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad "
                + "INNER JOIN usuario u ON u.id_usuario = s.id_cliente "
                + "LEFT JOIN perfil pf ON pf.id_usuario = u.id_usuario "
                + (manager ? "" : "WHERE s.id_cliente = ? ")
                + "ORDER BY s.creada_en DESC, s.id_solicitud DESC";
        List<PropertyRequest> requests = new ArrayList<>();
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!manager) {
                statement.setInt(1, userId);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    requests.add(new PropertyRequest(result.getInt("id_solicitud"),
                            result.getInt("id_propiedad"), result.getString("titulo"),
                            result.getString("cliente"), result.getString("tipo_operacion"),
                            result.getString("estado"), result.getTimestamp("creada_en")));
                }
            }
        }
        return requests;
    }

    public void createAppointment(int userId, int propertyId, Timestamp dateTime,
            String observations) throws SQLException {
        String sql = "INSERT INTO cita (id_propiedad, id_cliente, fecha_hora, observaciones) "
                + "SELECT id_propiedad, ?, ?, ? FROM propiedad WHERE id_propiedad = ? AND disponible = TRUE";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setTimestamp(2, dateTime);
            statement.setString(3, nullable(observations));
            statement.setInt(4, propertyId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("La propiedad no existe o ya no está disponible.");
            }
        }
    }

    public void createRequest(int userId, int propertyId, String operationType) throws SQLException {
        String sql = "INSERT INTO solicitud (id_propiedad, id_cliente, tipo_operacion) "
                + "SELECT id_propiedad, ?, ? FROM propiedad WHERE id_propiedad = ? AND disponible = TRUE";
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, operationType);
            statement.setInt(3, propertyId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("La propiedad no existe o ya no está disponible.");
            }
        }
    }

    public void updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        updateStatus("UPDATE cita SET estado = ? WHERE id_cita = ?", appointmentId, status);
    }

    public void updateRequestStatus(int requestId, String status) throws SQLException {
        updateStatus("UPDATE solicitud SET estado = ? WHERE id_solicitud = ?", requestId, status);
    }

    private void updateStatus(String sql, int id, String status) throws SQLException {
        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("No se encontró el registro solicitado.");
            }
        }
    }

    private static String nullable(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}

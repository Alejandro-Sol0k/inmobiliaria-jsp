package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.OperationDao;
import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.Property;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/operaciones")
public final class OperationServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final List<String> APPOINTMENT_STATUSES = Arrays.asList("PENDIENTE", "CONFIRMADA", "CANCELADA", "ATENDIDA");
    private static final List<String> REQUEST_STATUSES = Arrays.asList("RADICADA", "EN_REVISION", "APROBADA", "RECHAZADA");
    private final OperationDao operationDao = new OperationDao();
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int userId = userId(request);
        String action = value(request, "accion");
        try {
            if ("crearCita".equals(action)) {
                LocalDateTime dateTime = LocalDateTime.parse(value(request, "fechaHora"), DATE_TIME_FORMAT);
                if (!dateTime.isAfter(LocalDateTime.now())) {
                    throw new IllegalArgumentException("La fecha de la cita debe ser futura.");
                }
                operationDao.createAppointment(userId, integer(request, "idPropiedad"),
                        Timestamp.valueOf(dateTime), value(request, "observaciones"));
                redirect(response, request, "cita=ok");
                return;
            }
            if ("crearSolicitud".equals(action)) {
                String operationType = value(request, "tipoOperacion").toUpperCase();
                if (!Arrays.asList("COMPRA", "ARRIENDO").contains(operationType)) {
                    throw new IllegalArgumentException("Selecciona un tipo de solicitud válido.");
                }
                operationDao.createRequest(userId, integer(request, "idPropiedad"), operationType);
                redirect(response, request, "solicitud=ok");
                return;
            }
            if (isManager(request) && "estadoCita".equals(action)) {
                updateStatus(value(request, "estado"), APPOINTMENT_STATUSES);
                operationDao.updateAppointmentStatus(integer(request, "idCita"), value(request, "estado").toUpperCase());
                redirect(response, request, "actualizado=ok");
                return;
            }
            if (isManager(request) && "estadoSolicitud".equals(action)) {
                updateStatus(value(request, "estado"), REQUEST_STATUSES);
                operationDao.updateRequestStatus(integer(request, "idSolicitud"), value(request, "estado").toUpperCase());
                redirect(response, request, "actualizado=ok");
                return;
            }
            throw new IllegalArgumentException("Acción no válida.");
        } catch (Exception exception) {
            request.setAttribute("errorOperaciones", exception.getMessage() == null
                    ? "No fue posible completar la operación." : exception.getMessage());
            loadPage(request, response);
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = userId(request);
        boolean manager = isManager(request);
        try {
            request.setAttribute("propiedades", propertyDao.findPublic("", "", "", null));
            request.setAttribute("citas", operationDao.findAppointments(userId, manager));
            request.setAttribute("solicitudes", operationDao.findRequests(userId, manager));
        } catch (Exception exception) {
            request.setAttribute("propiedades", Collections.emptyList());
            request.setAttribute("citas", Collections.emptyList());
            request.setAttribute("solicitudes", Collections.emptyList());
            request.setAttribute("errorOperaciones", "No fue posible cargar las citas y solicitudes.");
            getServletContext().log("Error cargando operaciones", exception);
        }
        request.setAttribute("esGestor", manager);
        request.setAttribute("tituloPagina", "Citas y solicitudes");
        request.getRequestDispatcher("/app/operaciones.jsp").forward(request, response);
    }

    private static void updateStatus(String status, List<String> allowed) {
        if (!allowed.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("El estado seleccionado no es válido.");
        }
    }

    private static void redirect(HttpServletResponse response, HttpServletRequest request, String query)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/app/operaciones?" + query);
    }

    private static boolean isManager(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "INMOBILIARIA".equals(role) || "ADMINISTRADOR".equals(role);
    }

    private static int userId(HttpServletRequest request) {
        return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue();
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private static int integer(HttpServletRequest request, String name) {
        return Integer.parseInt(value(request, name));
    }
}

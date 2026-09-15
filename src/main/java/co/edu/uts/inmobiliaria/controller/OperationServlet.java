package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.OperationDao;
import co.edu.uts.inmobiliaria.dao.DocumentDao;
import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.dao.AuditDao;
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
    private final DocumentDao documentDao = new DocumentDao();
    private final PropertyDao propertyDao = new PropertyDao();
    private final AuditDao auditDao = new AuditDao();

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
                requireClient(request);
                LocalDateTime dateTime = LocalDateTime.parse(value(request, "fechaHora"), DATE_TIME_FORMAT);
                if (!dateTime.isAfter(LocalDateTime.now())) {
                    throw new IllegalArgumentException("La fecha de la cita debe ser futura.");
                }
                operationDao.createAppointment(userId, integer(request, "idPropiedad"),
                        Timestamp.valueOf(dateTime), value(request, "observaciones"));
                auditDao.log(userId, "CREAR", "cita", null, "Solicitud de visita para propiedad " + value(request, "idPropiedad"));
                redirect(response, request, "success", "La cita fue solicitada correctamente.");
                return;
            }
            if ("crearSolicitud".equals(action)) {
                requireClient(request);
                String operationType = value(request, "tipoOperacion").toUpperCase();
                if (!Arrays.asList("COMPRA", "ARRIENDO").contains(operationType)) {
                    throw new IllegalArgumentException("Selecciona un tipo de solicitud válido.");
                }
                operationDao.createRequest(userId, integer(request, "idPropiedad"), operationType);
                auditDao.log(userId, "CREAR", "solicitud", null, "Solicitud de " + operationType + " para propiedad " + value(request, "idPropiedad"));
                redirect(response, request, "success", "La solicitud fue radicada correctamente.");
                return;
            }
            if (isManager(request) && "estadoCita".equals(action)) {
                String status = value(request, "estado").toUpperCase();
                updateStatus(status, APPOINTMENT_STATUSES);
                operationDao.updateAppointmentStatus(integer(request, "idCita"), status);
                auditDao.log(userId, "CAMBIAR_ESTADO", "cita", value(request, "idCita"), status);
                redirect(response, request, "success", "El estado fue actualizado correctamente.");
                return;
            }
            if (isManager(request) && "estadoSolicitud".equals(action)) {
                String status = value(request, "estado").toUpperCase();
                updateStatus(status, REQUEST_STATUSES);
                operationDao.updateRequestStatus(integer(request, "idSolicitud"), status);
                auditDao.log(userId, "CAMBIAR_ESTADO", "solicitud", value(request, "idSolicitud"), status);
                redirect(response, request, "success", "El estado fue actualizado correctamente.");
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
        request.setAttribute("mensajeOperaciones", SessionState.consumeFlash(request.getSession(false), "success"));
        String flashError = SessionState.consumeFlash(request.getSession(false), "error");
        if (flashError != null) request.setAttribute("errorOperaciones", flashError);
        String legacyProperty = value(request, "propiedad");
        if (legacyProperty.isEmpty()) legacyProperty = value(request, "idPropiedad");
        if (!legacyProperty.isEmpty()) {
            request.getSession(true).setAttribute(SessionState.SELECTED_PROPERTY, Integer.valueOf(legacyProperty));
            response.sendRedirect(request.getContextPath() + "/app/operaciones");
            return;
        }
        int userId = userId(request);
        boolean manager = isManager(request);
        try {
            request.setAttribute("propiedades", propertyDao.findPublic("", "", "", null));
            String selectedProperty = value(request, "propiedad");
            if (selectedProperty.isEmpty()) selectedProperty = value(request, "idPropiedad");
            if (!selectedProperty.isEmpty()) {
                request.getSession(true).setAttribute(SessionState.SELECTED_PROPERTY, Integer.valueOf(selectedProperty));
            } else {
                Integer sessionProperty = SessionState.integerAttribute(request.getSession(false), SessionState.SELECTED_PROPERTY);
                selectedProperty = sessionProperty == null ? "" : String.valueOf(sessionProperty);
            }
            request.setAttribute("propiedadSeleccionada", selectedProperty);
            request.setAttribute("citas", operationDao.findAppointments(userId, manager));
            request.setAttribute("solicitudes", operationDao.findRequests(userId, manager));
            request.setAttribute("documentos", documentDao.findDocuments(userId, manager));
        } catch (Exception exception) {
            request.setAttribute("propiedades", Collections.emptyList());
            request.setAttribute("citas", Collections.emptyList());
            request.setAttribute("solicitudes", Collections.emptyList());
            request.setAttribute("documentos", Collections.emptyList());
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

    private static void redirect(HttpServletResponse response, HttpServletRequest request, String key, String message)
            throws IOException {
        SessionState.flash(request.getSession(false), key, message);
        response.sendRedirect(request.getContextPath() + "/app/operaciones");
    }

    private static boolean isManager(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "INMOBILIARIA".equals(role) || "ADMINISTRADOR".equals(role);
    }

    private static void requireClient(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        if (!"CLIENTE".equals(role)) {
            throw new IllegalArgumentException("Solo un cliente autenticado puede crear citas o solicitudes.");
        }
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

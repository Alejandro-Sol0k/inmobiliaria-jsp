package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.AdminDao;
import co.edu.uts.inmobiliaria.dao.AuditDao;
import co.edu.uts.inmobiliaria.config.Database;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/admin")
public final class AdminServlet extends HttpServlet {
    private static final List<String> ROLES = Arrays.asList("ADMINISTRADOR", "INMOBILIARIA", "CLIENTE", "VISITANTE");
    private final AdminDao adminDao = new AdminDao();
    private final AuditDao auditDao = new AuditDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) { response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo el administrador puede acceder a este panel."); return; }
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) { response.sendError(HttpServletResponse.SC_FORBIDDEN); return; }
        try {
            String action = value(request, "accion");
            if ("crearUsuario".equals(action)) {
                String email = value(request, "correo").toLowerCase();
                String password = value(request, "password");
                String role = value(request, "rol").toUpperCase();
                if (email.isEmpty() || password.length() < 8 || value(request, "nombres").isEmpty()
                        || value(request, "apellidos").isEmpty() || value(request, "documento").isEmpty()) {
                    throw new IllegalArgumentException("Completa todos los datos y usa una contraseña de mínimo 8 caracteres.");
                }
                if (!ROLES.contains(role)) throw new IllegalArgumentException("Rol no válido.");
                int createdId = adminDao.createUser(email, password, value(request, "nombres"),
                        value(request, "apellidos"), value(request, "documento"), role);
                auditDao.log(currentUserId(request), "CREAR_USUARIO", "usuario", String.valueOf(createdId), role);
                redirect(response, request, "creado=ok");
            } else if ("rol".equals(action)) {
                String role = value(request, "rol").toUpperCase();
                if (!ROLES.contains(role)) throw new IllegalArgumentException("Rol no válido.");
                adminDao.setRole(integer(request, "idUsuario"), role);
                auditDao.log(currentUserId(request), "ASIGNAR_ROL", "usuario", value(request, "idUsuario"), role);
                redirect(response, request, "rol=ok");
            } else if ("estado".equals(action)) {
                adminDao.toggleUser(integer(request, "idUsuario"), currentUserId(request));
                auditDao.log(currentUserId(request), "CAMBIAR_ESTADO", "usuario", value(request, "idUsuario"), "Estado de cuenta actualizado");
                redirect(response, request, "estado=ok");
            } else if ("catalogo".equals(action)) {
                String name = value(request, "nombre");
                if (name.isEmpty()) throw new IllegalArgumentException("Escribe un nombre para el catálogo.");
                adminDao.addCatalog(value(request, "catalogo"), name);
                auditDao.log(currentUserId(request), "CREAR_CATALOGO", value(request, "catalogo"), null, name);
                redirect(response, request, "catalogo=ok");
            } else throw new IllegalArgumentException("Acción no válida.");
        } catch (Exception exception) {
            request.setAttribute("errorAdmin", exception.getMessage() == null ? "No fue posible completar la acción." : exception.getMessage());
            loadPage(request, response);
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("usuarios", adminDao.findUsers());
            request.setAttribute("ciudades", adminDao.findCatalog("ciudad"));
            request.setAttribute("tipos", adminDao.findCatalog("tipo"));
            request.setAttribute("caracteristicas", adminDao.findCatalog("caracteristica"));
            request.setAttribute("auditoria", auditDao.findRecent(30));
        } catch (Exception exception) {
            request.setAttribute("errorAdmin", "No fue posible cargar el panel administrativo.");
            getServletContext().log("Error en panel administrativo", exception);
            request.setAttribute("auditoria", Collections.emptyList());
        }
        request.setAttribute("tituloPagina", "Administración");
        request.setAttribute("sincronizacionAutomatica", Database.isRemoteConfigured() && Database.isLocalSyncEnabled());
        request.setAttribute("sincronizacionIntervalo", Database.getSyncIntervalSeconds());
        request.getRequestDispatcher("/app/admin.jsp").forward(request, response);
    }

    private static boolean isAdmin(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "ADMINISTRADOR".equals(role);
    }
    private static int currentUserId(HttpServletRequest request) { return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue(); }
    private static int integer(HttpServletRequest request, String name) { return Integer.parseInt(value(request, name)); }
    private static String value(HttpServletRequest request, String name) { String value = request.getParameter(name); return value == null ? "" : value.trim(); }
    private static void redirect(HttpServletResponse response, HttpServletRequest request, String query) throws IOException { response.sendRedirect(request.getContextPath() + "/app/admin?" + query); }
}

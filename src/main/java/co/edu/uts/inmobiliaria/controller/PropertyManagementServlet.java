package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.Property;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/gestion-propiedades")
public final class PropertyManagementServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para gestionar propiedades.");
            return;
        }
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para gestionar propiedades.");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        try {
            if ("desactivar".equals(request.getParameter("accion"))) {
                propertyDao.deactivate(Integer.parseInt(value(request, "idPropiedad")));
                response.sendRedirect(request.getContextPath() + "/app/gestion-propiedades?desactivada=ok");
                return;
            }
            propertyDao.create(value(request, "titulo"), value(request, "ciudad"), value(request, "tipo"),
                    value(request, "operacion").toUpperCase(), value(request, "matricula"), value(request, "direccion"),
                    value(request, "descripcion"), decimal(request, "precio"), integer(request, "habitaciones"),
                    integer(request, "banos"), decimal(request, "area"));
            response.sendRedirect(request.getContextPath() + "/app/gestion-propiedades?creada=ok");
        } catch (Exception exception) {
            request.setAttribute("errorGestion", exception.getMessage() == null
                    ? "No fue posible crear la propiedad." : exception.getMessage());
            loadPage(request, response);
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Property> properties = propertyDao.findPublic("", "", "", null);
            request.setAttribute("propiedades", properties);
        } catch (Exception exception) {
            request.setAttribute("propiedades", Collections.emptyList());
            request.setAttribute("errorGestion", "No fue posible consultar las propiedades.");
            getServletContext().log("Error en la gestión de propiedades", exception);
        }
        request.setAttribute("tituloPagina", "Gestión de propiedades");
        request.getRequestDispatcher("/app/gestion-propiedades.jsp").forward(request, response);
    }

    private static boolean isManager(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "INMOBILIARIA".equals(role) || "ADMINISTRADOR".equals(role);
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private static int integer(HttpServletRequest request, String name) {
        return Integer.parseInt(value(request, name));
    }

    private static BigDecimal decimal(HttpServletRequest request, String name) {
        return new BigDecimal(value(request, name));
    }
}

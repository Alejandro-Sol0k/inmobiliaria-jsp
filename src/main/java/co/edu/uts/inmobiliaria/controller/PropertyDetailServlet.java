package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.Property;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/propiedad.jsp")
public final class PropertyDetailServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            int propertyId = Integer.parseInt(value(request, "id"));
            request.getSession(true).setAttribute(SessionState.SELECTED_PROPERTY, propertyId);
            response.sendRedirect(request.getContextPath() + "/propiedad.jsp");
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identificador de propiedad no válido.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String requestedId = value(request, "id");
            int propertyId;
            if (!requestedId.isEmpty()) {
                propertyId = Integer.parseInt(requestedId);
                request.getSession(true).setAttribute(SessionState.SELECTED_PROPERTY, propertyId);
                response.sendRedirect(request.getContextPath() + "/propiedad.jsp");
                return;
            } else {
                Integer selectedId = SessionState.integerAttribute(session, SessionState.SELECTED_PROPERTY);
                if (selectedId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Selecciona una propiedad para ver su detalle.");
                    return;
                }
                propertyId = selectedId;
            }
            Property property = propertyDao.findActiveById(propertyId);
            if (property == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "La propiedad no está disponible.");
                return;
            }
            request.setAttribute("propiedad", property);
            request.setAttribute("imagenes", propertyDao.findImageUrls(propertyId));
            request.setAttribute("caracteristicas", propertyDao.findFeatureNames(propertyId));
            if (request.getSession(false) == null
                    || request.getSession(false).getAttribute("usuarioId") == null) {
                request.getSession(true).setAttribute(SessionState.LOGIN_TARGET, "/propiedad.jsp");
            }
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identificador de propiedad no válido.");
            return;
        } catch (Exception exception) {
            request.setAttribute("imagenes", Collections.emptyList());
            request.setAttribute("caracteristicas", Collections.emptyList());
            request.setAttribute("errorDetalle", "No fue posible cargar el detalle de la propiedad.");
            getServletContext().log("Error cargando detalle de propiedad", exception);
        }
        request.setAttribute("tituloPagina", "Detalle de propiedad");
        request.getRequestDispatcher("/detalle-propiedad.jsp").forward(request, response);
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}

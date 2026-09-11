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

@WebServlet("/propiedad")
public final class PropertyDetailServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int propertyId = Integer.parseInt(value(request, "id"));
            Property property = propertyDao.findActiveById(propertyId);
            if (property == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "La propiedad no está disponible.");
                return;
            }
            request.setAttribute("propiedad", property);
            request.setAttribute("imagenes", propertyDao.findImageUrls(propertyId));
            request.setAttribute("caracteristicas", propertyDao.findFeatureNames(propertyId));
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

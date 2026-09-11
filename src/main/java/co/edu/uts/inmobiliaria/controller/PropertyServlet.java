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

@WebServlet("/propiedades")
public final class PropertyServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String city = value(request, "ciudad");
        String type = value(request, "tipo");
        String operation = value(request, "operacion").toUpperCase();
        BigDecimal maxPrice = decimal(value(request, "precioMax"));
        request.setAttribute("tituloPagina", "Propiedades");
        request.setAttribute("filtroCiudad", city);
        request.setAttribute("filtroTipo", type);
        request.setAttribute("filtroOperacion", operation);
        request.setAttribute("filtroPrecioMax", value(request, "precioMax"));
        try {
            List<Property> properties = propertyDao.findPublic(city, type, operation, maxPrice);
            request.setAttribute("propiedades", properties);
        } catch (Exception exception) {
            request.setAttribute("propiedades", Collections.emptyList());
            request.setAttribute("errorCatalogo", "No fue posible consultar el catálogo. Verifica la conexión con la base de datos.");
            getServletContext().log("Error consultando el catálogo público", exception);
        }
        request.getRequestDispatcher("/catalogo.jsp").forward(request, response);
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private static BigDecimal decimal(String value) {
        if (value.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.replace(".", "").replace(",", ""));
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}

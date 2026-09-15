package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.Property;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/propiedades.jsp")
public final class PropertyServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if ("limpiar".equals(value(request, "accion"))) {
            request.getSession(true).removeAttribute(SessionState.CATALOG_FILTERS);
        } else {
            Map<String, String> filters = new HashMap<String, String>();
            filters.put("ciudad", value(request, "ciudad"));
            filters.put("tipo", value(request, "tipo"));
            filters.put("operacion", value(request, "operacion"));
            filters.put("precioMax", value(request, "precioMax"));
            request.getSession(true).setAttribute(SessionState.CATALOG_FILTERS, filters);
        }
        response.sendRedirect(request.getContextPath() + "/propiedades.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (hasFilterParameter(request)) {
            filters(request);
            response.sendRedirect(request.getContextPath() + "/propiedades.jsp");
            return;
        }
        Map<String, String> filters = filters(request);
        String city = filters.get("ciudad");
        String type = filters.get("tipo");
        String operation = filters.get("operacion").toUpperCase();
        BigDecimal maxPrice = decimal(filters.get("precioMax"));
        request.setAttribute("tituloPagina", "Propiedades");
        request.setAttribute("filtroCiudad", city);
        request.setAttribute("filtroTipo", type);
        request.setAttribute("filtroOperacion", operation);
        request.setAttribute("filtroPrecioMax", filters.get("precioMax"));
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

    @SuppressWarnings("unchecked")
    private static Map<String, String> filters(HttpServletRequest request) {
        Object stored = request.getSession(true).getAttribute(SessionState.CATALOG_FILTERS);
        if (stored instanceof Map) {
            Map<String, String> filters = (Map<String, String>) stored;
            return normalize(filters);
        }
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("ciudad", value(request, "ciudad"));
        filters.put("tipo", value(request, "tipo"));
        filters.put("operacion", value(request, "operacion"));
        filters.put("precioMax", value(request, "precioMax"));
        if (!filters.get("ciudad").isEmpty() || !filters.get("tipo").isEmpty()
                || !filters.get("operacion").isEmpty() || !filters.get("precioMax").isEmpty()) {
            request.getSession(true).setAttribute(SessionState.CATALOG_FILTERS, filters);
        }
        return filters;
    }

    private static Map<String, String> normalize(Map<String, String> filters) {
        for (String key : new String[]{"ciudad", "tipo", "operacion", "precioMax"}) {
            if (!filters.containsKey(key) || filters.get(key) == null) {
                filters.put(key, "");
            }
        }
        return filters;
    }

    private static boolean hasFilterParameter(HttpServletRequest request) {
        return request.getParameter("ciudad") != null || request.getParameter("tipo") != null
                || request.getParameter("operacion") != null || request.getParameter("precioMax") != null;
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

package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.Property;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/inicio.jsp")
public final class HomeServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Property> properties = propertyDao.findPublic("", "", "", null);
            request.setAttribute("propiedadesDestacadas", properties);
        } catch (Exception exception) {
            request.setAttribute("propiedadesDestacadas", Collections.emptyList());
            getServletContext().log("Error cargando propiedades destacadas", exception);
        }
        request.setAttribute("tituloPagina", "Inicio");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}

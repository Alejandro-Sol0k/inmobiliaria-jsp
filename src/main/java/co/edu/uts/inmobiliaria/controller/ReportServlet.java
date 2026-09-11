package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.ReportDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/reportes")
public final class ReportServlet extends HttpServlet {
    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para consultar reportes.");
            return;
        }
        try {
            request.setAttribute("ciudades", reportDao.activePropertiesByCity());
            request.setAttribute("operaciones", reportDao.propertiesByOperation());
            request.setAttribute("solicitudesEstado", reportDao.requestsByStatus());
            request.setAttribute("citasEstado", reportDao.appointmentsByStatus());
        } catch (Exception exception) {
            request.setAttribute("errorReportes", "No fue posible generar los reportes.");
            getServletContext().log("Error generando reportes", exception);
        }
        request.setAttribute("tituloPagina", "Reportes");
        request.getRequestDispatcher("/app/reportes.jsp").forward(request, response);
    }

    private static boolean isAdmin(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "ADMINISTRADOR".equals(role);
    }
}

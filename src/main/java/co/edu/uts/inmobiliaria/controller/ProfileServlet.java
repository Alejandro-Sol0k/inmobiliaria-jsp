package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.ProfileDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/perfil")
public final class ProfileServlet extends HttpServlet {
    private final ProfileDao profileDao = new ProfileDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadPage(request, response, userId(request));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String names = value(request, "nombres");
        String lastNames = value(request, "apellidos");
        String document = value(request, "documento");
        if (names.isEmpty() || lastNames.isEmpty() || document.isEmpty()) {
            request.setAttribute("errorPerfil", "Nombres, apellidos y documento son obligatorios.");
            loadPage(request, response, userId(request));
            return;
        }
        try {
            profileDao.update(userId(request), names, lastNames, document,
                    value(request, "telefono"), value(request, "direccion"), value(request, "fotoUrl"));
            request.getSession(false).setAttribute("usuarioNombres", names);
            request.getSession(false).setAttribute("usuarioApellidos", lastNames);
            request.getSession(false).setAttribute("usuarioFotoUrl", value(request, "fotoUrl"));
            response.sendRedirect(request.getContextPath() + "/app/perfil?actualizado=ok");
        } catch (Exception exception) {
            request.setAttribute("errorPerfil", exception.getMessage() == null
                    ? "No fue posible actualizar el perfil." : exception.getMessage());
            loadPage(request, response, userId(request));
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        try {
            request.setAttribute("perfil", profileDao.findByUserId(userId));
        } catch (Exception exception) {
            request.setAttribute("errorPerfil", "No fue posible consultar el perfil.");
            getServletContext().log("Error consultando el perfil", exception);
        }
        request.setAttribute("tituloPagina", "Mi perfil");
        request.getRequestDispatcher("/app/perfil.jsp").forward(request, response);
    }

    private static int userId(HttpServletRequest request) {
        return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue();
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}

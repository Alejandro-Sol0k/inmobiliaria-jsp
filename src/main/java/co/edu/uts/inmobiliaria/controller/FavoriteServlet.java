package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.FavoriteDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/favoritos")
public final class FavoriteServlet extends HttpServlet {
    private final FavoriteDao favoriteDao = new FavoriteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mensajeFavoritos", SessionState.consumeFlash(request.getSession(false), "success"));
        String flashError = SessionState.consumeFlash(request.getSession(false), "error");
        if (flashError != null) request.setAttribute("errorFavoritos", flashError);
        try {
            request.setAttribute("favoritos", favoriteDao.findByUserId(userId(request)));
        } catch (Exception exception) {
            request.setAttribute("errorFavoritos", "No fue posible cargar tus favoritos.");
            getServletContext().log("Error cargando favoritos", exception);
        }
        request.setAttribute("tituloPagina", "Mis favoritos");
        request.getRequestDispatcher("/app/favoritos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            requireClient(request);
            int propertyId = Integer.parseInt(value(request, "idPropiedad"));
            if ("quitar".equals(value(request, "accion"))) {
                favoriteDao.remove(userId(request), propertyId);
            } else {
                favoriteDao.add(userId(request), propertyId);
            }
            SessionState.flash(request.getSession(false), "success", "Tus favoritos fueron actualizados.");
            response.sendRedirect(request.getContextPath() + "/app/favoritos");
        } catch (Exception exception) {
            SessionState.flash(request.getSession(false), "error", "No fue posible actualizar tus favoritos.");
            response.sendRedirect(request.getContextPath() + "/app/favoritos");
        }
    }

    private static int userId(HttpServletRequest request) {
        return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue();
    }

    private static void requireClient(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        if (!"CLIENTE".equals(role)) {
            throw new IllegalArgumentException("Solo un cliente autenticado puede guardar favoritos.");
        }
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}

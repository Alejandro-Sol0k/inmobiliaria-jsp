package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.UserDao;
import co.edu.uts.inmobiliaria.model.AuthenticatedUser;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/auth/login")
public final class LoginServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = value(request, "correo").toLowerCase();
        String password = value(request, "password");
        if (email.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorLogin", "Ingresa tu correo y contrasena.");
            doGet(request, response);
            return;
        }
        try {
            AuthenticatedUser user = userDao.authenticate(email, password);
            if (user == null) {
                request.setAttribute("errorLogin", "El correo o la contrasena no son validos.");
                doGet(request, response);
                return;
            }
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioId", user.getId());
            session.setAttribute("usuarioCorreo", user.getEmail());
            session.setAttribute("usuarioRol", user.getRole());
            response.sendRedirect(request.getContextPath() + "/app/dashboard.jsp");
        } catch (Exception exception) {
            request.setAttribute("errorLogin", "No fue posible iniciar sesion. Intenta de nuevo.");
            doGet(request, response);
        }
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}

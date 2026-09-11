package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.UserDao;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth/registro")
public final class RegisterServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = value(request, "correo").toLowerCase();
        String names = value(request, "nombres");
        String lastNames = value(request, "apellidos");
        String document = value(request, "documento");
        String password = value(request, "password");
        if (email.isEmpty() || names.isEmpty() || lastNames.isEmpty()
                || document.isEmpty() || password.length() < 8) {
            request.setAttribute("errorRegistro", "Completa todos los campos y usa una contrasena de al menos 8 caracteres.");
            doGet(request, response);
            return;
        }
        try {
            userDao.registerClient(email, password, names, lastNames, document);
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp?registro=ok");
        } catch (SQLException exception) {
            request.setAttribute("errorRegistro", exception.getErrorCode() == 1062
                    ? "El correo o documento ya se encuentra registrado."
                    : "No fue posible crear la cuenta.");
            doGet(request, response);
        } catch (Exception exception) {
            request.setAttribute("errorRegistro", "No fue posible crear la cuenta. Intenta de nuevo.");
            doGet(request, response);
        }
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}

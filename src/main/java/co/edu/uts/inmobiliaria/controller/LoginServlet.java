package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.UserDao;
import co.edu.uts.inmobiliaria.dao.ProfileDao;
import co.edu.uts.inmobiliaria.dao.AuditDao;
import co.edu.uts.inmobiliaria.model.AuthenticatedUser;
import co.edu.uts.inmobiliaria.model.Profile;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/auth/login.jsp")
public final class LoginServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final ProfileDao profileDao = new ProfileDao();
    private final AuditDao auditDao = new AuditDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String legacyRedirect = safeRedirect(value(request, "redirect"));
        if (!value(request, "redirect").isEmpty()) {
            request.getSession(true).setAttribute(SessionState.LOGIN_TARGET, legacyRedirect);
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        request.setAttribute("mensajeLogin", SessionState.consumeFlash(request.getSession(false), "success"));
        request.getRequestDispatcher("/auth/login-form.jsp").forward(request, response);
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
            if (user == null || "VISITANTE".equals(user.getRole())) {
                auditDao.log(null, "LOGIN_FALLIDO", "usuario", null, email);
                request.setAttribute("errorLogin", "El correo o la contrasena no son validos.");
                doGet(request, response);
                return;
            }
            HttpSession oldSession = request.getSession(false);
            Integer selectedProperty = SessionState.integerAttribute(oldSession, SessionState.SELECTED_PROPERTY);
            String loginTarget = oldSession == null ? null : (String) oldSession.getAttribute(SessionState.LOGIN_TARGET);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioId", user.getId());
            session.setAttribute("usuarioCorreo", user.getEmail());
            session.setAttribute("usuarioRol", user.getRole());
            Profile profile = profileDao.findByUserId(user.getId());
            if (profile != null) {
                session.setAttribute("usuarioNombres", profile.getNames());
                session.setAttribute("usuarioApellidos", profile.getLastNames());
                session.setAttribute("usuarioFotoUrl", profile.getPhotoUrl());
            }
            if (selectedProperty != null) {
                session.setAttribute(SessionState.SELECTED_PROPERTY, selectedProperty);
            }
            auditDao.log(user.getId(), "LOGIN", "usuario", String.valueOf(user.getId()), "Inicio de sesión exitoso");
            response.sendRedirect(request.getContextPath() + safeRedirect(loginTarget));
        } catch (Exception exception) {
            request.setAttribute("errorLogin", "No fue posible iniciar sesion. Intenta de nuevo.");
            doGet(request, response);
        }
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private static String safeRedirect(String redirect) {
        if (redirect == null || redirect.isEmpty() || !redirect.startsWith("/")
                || redirect.startsWith("//") || redirect.contains("://")) {
            return "/propiedades.jsp";
        }
        return redirect;
    }
}

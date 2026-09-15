package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.ProfileDao;
import co.edu.uts.inmobiliaria.dao.AuditDao;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/app/perfil.jsp")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public final class ProfileServlet extends HttpServlet {
    private final ProfileDao profileDao = new ProfileDao();
    private final AuditDao auditDao = new AuditDao();

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
            String photoUrl = value(request, "fotoUrl");
            Part photoPart = request.getPart("fotoArchivo");
            if (photoPart != null && photoPart.getSize() > 0) {
                photoUrl = saveUploadedPhoto(photoPart, request);
            }
            profileDao.update(userId(request), names, lastNames, document,
                    value(request, "telefono"), value(request, "direccion"), photoUrl);
            auditDao.log(userId(request), "ACTUALIZAR", "perfil", String.valueOf(userId(request)), "Datos personales actualizados");
            request.getSession(false).setAttribute("usuarioNombres", names);
            request.getSession(false).setAttribute("usuarioApellidos", lastNames);
            request.getSession(false).setAttribute("usuarioFotoUrl", photoUrl);
            SessionState.flash(request.getSession(false), "success", "Tu perfil fue actualizado correctamente.");
            response.sendRedirect(request.getContextPath() + "/app/perfil.jsp");
        } catch (Exception exception) {
            request.setAttribute("errorPerfil", exception.getMessage() == null
                    ? "No fue posible actualizar el perfil." : exception.getMessage());
            loadPage(request, response, userId(request));
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        request.setAttribute("mensajePerfil", SessionState.consumeFlash(request.getSession(false), "success"));
        try {
            request.setAttribute("perfil", profileDao.findByUserId(userId));
        } catch (Exception exception) {
            request.setAttribute("errorPerfil", "No fue posible consultar el perfil.");
            getServletContext().log("Error consultando el perfil", exception);
        }
        request.setAttribute("tituloPagina", "Mi perfil");
        request.getRequestDispatcher("/WEB-INF/views/perfil.jsp").forward(request, response);
    }

    private static int userId(HttpServletRequest request) {
        return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue();
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private String saveUploadedPhoto(Part photoPart, HttpServletRequest request) throws IOException {
        String originalName = photoPart.getSubmittedFileName();
        String extension = "";
        if (originalName != null) {
            int dot = originalName.lastIndexOf('.');
            if (dot >= 0) {
                extension = originalName.substring(dot).toLowerCase(Locale.ROOT);
            }
        }
        if (!Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp").contains(extension)) {
            throw new IOException("La foto debe ser JPG, PNG, GIF o WEBP.");
        }
        String fileName = UUID.randomUUID().toString() + extension;
        String realDirectory = getServletContext().getRealPath("/assets/images/uploads/profiles");
        if (realDirectory == null) {
            throw new IOException("Tomcat no tiene disponible la carpeta de fotos.");
        }
        Path directory = Paths.get(realDirectory);
        Files.createDirectories(directory);
        photoPart.write(directory.resolve(fileName).toString());
        return request.getContextPath() + "/assets/images/uploads/profiles/" + fileName;
    }
}

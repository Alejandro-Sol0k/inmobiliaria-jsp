package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.DocumentDao;
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

@WebServlet("/app/documentos")
@MultipartConfig(maxFileSize = 8 * 1024 * 1024, maxRequestSize = 9 * 1024 * 1024)
public final class DocumentServlet extends HttpServlet {
    private final DocumentDao documentDao = new DocumentDao();
    private final AuditDao auditDao = new AuditDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            if ("estadoDocumento".equals(value(request, "accion"))) {
                if (!isManager(request)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                String status = value(request, "estado").toUpperCase();
                if (!Arrays.asList("PENDIENTE", "APROBADO", "RECHAZADO").contains(status)) {
                    throw new IOException("Estado de documento no válido.");
                }
                documentDao.updateStatus(Integer.parseInt(value(request, "idDocumento")), status);
                auditDao.log(userId(request), "CAMBIAR_ESTADO", "documento_solicitud", value(request, "idDocumento"), status);
                response.sendRedirect(request.getContextPath() + "/app/operaciones?documento=actualizado");
                return;
            }
            Part file = request.getPart("archivo");
            if (file == null || file.getSize() == 0) {
                throw new IOException("Selecciona un documento para cargar.");
            }
            String fileUrl = saveFile(file, request);
            documentDao.create(userId(request), Integer.parseInt(value(request, "idSolicitud")),
                    safeFileName(file.getSubmittedFileName()), fileUrl);
            auditDao.log(userId(request), "CARGAR", "documento_solicitud", value(request, "idSolicitud"), safeFileName(file.getSubmittedFileName()));
            response.sendRedirect(request.getContextPath() + "/app/operaciones?documento=ok");
        } catch (Exception exception) {
            response.sendRedirect(request.getContextPath() + "/app/operaciones?documentoError=1");
        }
    }

    private String saveFile(Part file, HttpServletRequest request) throws IOException {
        String originalName = file.getSubmittedFileName();
        String extension = "";
        if (originalName != null && originalName.lastIndexOf('.') >= 0) {
            extension = originalName.substring(originalName.lastIndexOf('.')).toLowerCase(Locale.ROOT);
        }
        if (!Arrays.asList(".pdf", ".jpg", ".jpeg", ".png").contains(extension)) {
            throw new IOException("El documento debe ser PDF, JPG o PNG.");
        }
        String fileName = UUID.randomUUID().toString() + extension;
        String realDirectory = getServletContext().getRealPath("/assets/uploads/documents");
        if (realDirectory == null) {
            throw new IOException("Tomcat no tiene disponible la carpeta de documentos.");
        }
        Path directory = Paths.get(realDirectory);
        Files.createDirectories(directory);
        file.write(directory.resolve(fileName).toString());
        return request.getContextPath() + "/assets/uploads/documents/" + fileName;
    }

    private static String safeFileName(String fileName) {
        return fileName == null || fileName.trim().isEmpty() ? "documento" : Paths.get(fileName).getFileName().toString();
    }

    private static int userId(HttpServletRequest request) {
        return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue();
    }

    private static boolean isManager(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "INMOBILIARIA".equals(role) || "ADMINISTRADOR".equals(role);
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}

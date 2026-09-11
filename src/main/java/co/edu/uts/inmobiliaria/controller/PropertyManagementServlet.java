package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.Property;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/app/gestion-propiedades")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public final class PropertyManagementServlet extends HttpServlet {
    private final PropertyDao propertyDao = new PropertyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para gestionar propiedades.");
            return;
        }
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para gestionar propiedades.");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        try {
            String action = value(request, "accion");
            if ("desactivar".equals(action)) {
                propertyDao.deactivate(Integer.parseInt(value(request, "idPropiedad")));
                response.sendRedirect(request.getContextPath() + "/app/gestion-propiedades?desactivada=ok");
                return;
            }
            String imageUrl = value(request, "imagenUrl");
            Part imagePart = request.getPart("imagenArchivo");
            if (imagePart != null && imagePart.getSize() > 0) {
                imageUrl = saveUploadedImage(imagePart, request);
            }
            List<String> features = request.getParameterValues("caracteristicas") == null
                    ? Collections.<String>emptyList()
                    : Arrays.asList(request.getParameterValues("caracteristicas"));
            if ("actualizar".equals(action)) {
                propertyDao.update(Integer.parseInt(value(request, "idPropiedad")), value(request, "titulo"),
                        value(request, "ciudad"), value(request, "tipo"), value(request, "operacion").toUpperCase(),
                        value(request, "matricula"), value(request, "direccion"), value(request, "descripcion"),
                        decimal(request, "precio"), integer(request, "habitaciones"), integer(request, "banos"),
                        decimal(request, "area"), imageUrl, features);
                response.sendRedirect(request.getContextPath() + "/app/gestion-propiedades?actualizada=ok");
                return;
            }
            propertyDao.create(value(request, "titulo"), value(request, "ciudad"), value(request, "tipo"),
                    value(request, "operacion").toUpperCase(), value(request, "matricula"), value(request, "direccion"),
                    value(request, "descripcion"), decimal(request, "precio"), integer(request, "habitaciones"),
                    integer(request, "banos"), decimal(request, "area"), imageUrl, features);
            response.sendRedirect(request.getContextPath() + "/app/gestion-propiedades?creada=ok");
        } catch (Exception exception) {
            request.setAttribute("errorGestion", exception.getMessage() == null
                    ? "No fue posible guardar la propiedad." : exception.getMessage());
            loadPage(request, response);
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Property> properties = propertyDao.findPublic("", "", "", null);
            request.setAttribute("propiedades", properties);
            String editId = value(request, "editar");
            if (!editId.isEmpty()) {
                int propertyId = Integer.parseInt(editId);
                for (Property property : properties) {
                    if (property.getId() == propertyId) {
                        request.setAttribute("propiedadEditar", property);
                        request.setAttribute("caracteristicasEditar", propertyDao.findFeatureNames(propertyId));
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            request.setAttribute("propiedades", Collections.emptyList());
            request.setAttribute("errorGestion", "No fue posible consultar las propiedades.");
            getServletContext().log("Error en la gestión de propiedades", exception);
        }
        request.setAttribute("tituloPagina", "Gestión de propiedades");
        request.getRequestDispatcher("/app/gestion-propiedades.jsp").forward(request, response);
    }

    private static boolean isManager(HttpServletRequest request) {
        Object role = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioRol");
        return "INMOBILIARIA".equals(role) || "ADMINISTRADOR".equals(role);
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private static int integer(HttpServletRequest request, String name) {
        return Integer.parseInt(value(request, name));
    }

    private static BigDecimal decimal(HttpServletRequest request, String name) {
        String raw = value(request, name);
        if (raw.isEmpty()) {
            raw = value(request, name + "Visible");
        }
        return new BigDecimal(raw.replace(".", "").replace(",", ""));
    }

    private String saveUploadedImage(Part imagePart, HttpServletRequest request) throws IOException {
        String originalName = imagePart.getSubmittedFileName();
        String extension = "";
        if (originalName != null) {
            int dot = originalName.lastIndexOf('.');
            if (dot >= 0) {
                extension = originalName.substring(dot).toLowerCase(Locale.ROOT);
            }
        }
        if (!Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp").contains(extension)) {
            throw new IOException("El archivo debe ser una imagen JPG, PNG, GIF o WEBP.");
        }
        String fileName = UUID.randomUUID().toString() + extension;
        String realDirectory = getServletContext().getRealPath("/assets/images/uploads");
        if (realDirectory == null) {
            throw new IOException("Tomcat no tiene disponible la carpeta de imágenes.");
        }
        Path directory = Paths.get(realDirectory);
        Files.createDirectories(directory);
        imagePart.write(directory.resolve(fileName).toString());
        return request.getContextPath() + "/assets/images/uploads/" + fileName;
    }
}

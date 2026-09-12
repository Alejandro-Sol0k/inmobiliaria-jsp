package co.edu.uts.inmobiliaria.controller;

import co.edu.uts.inmobiliaria.dao.AuditDao;
import co.edu.uts.inmobiliaria.dao.ChatDao;
import co.edu.uts.inmobiliaria.dao.PropertyDao;
import co.edu.uts.inmobiliaria.model.ChatConversation;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/app/chat")
public final class ChatServlet extends HttpServlet {
    private final ChatDao chatDao = new ChatDao();
    private final PropertyDao propertyDao = new PropertyDao();
    private final AuditDao auditDao = new AuditDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!canUseChat(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "El chat requiere una cuenta autenticada.");
            return;
        }
        loadPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!canUseChat(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "El chat requiere una cuenta autenticada.");
            return;
        }
        int userId = userId(request);
        String role = role(request);
        try {
            String action = value(request, "accion");
            if ("crear".equals(action)) {
                requireRole(role, "CLIENTE");
                String subject = value(request, "asunto");
                String message = value(request, "mensaje");
                if (subject.isEmpty() || message.isEmpty()) {
                    throw new IllegalArgumentException("Escribe un asunto y un mensaje para iniciar el chat.");
                }
                int chatId = chatDao.createConversation(userId, integerOrZero(request, "idPropiedad"), subject, message);
                auditDao.log(userId, "CREAR", "chat", String.valueOf(chatId), subject);
                redirect(response, request, chatId, "creado=ok");
                return;
            }
            if ("mensaje".equals(action)) {
                String message = value(request, "mensaje");
                if (message.isEmpty()) throw new IllegalArgumentException("Escribe un mensaje.");
                int chatId = integer(request, "idChat");
                chatDao.addMessage(chatId, userId, message, role);
                auditDao.log(userId, "ENVIAR", "chat_mensaje", String.valueOf(chatId), "Mensaje enviado");
                redirect(response, request, chatId, "enviado=ok");
                return;
            }
            if ("cerrar".equals(action)) {
                if (!isManager(role)) throw new IllegalArgumentException("Solo la inmobiliaria puede cerrar el chat.");
                int chatId = integer(request, "idChat");
                chatDao.closeConversation(chatId, userId, role);
                auditDao.log(userId, "CERRAR", "chat", String.valueOf(chatId), "Conversación cerrada");
                redirect(response, request, chatId, "cerrado=ok");
                return;
            }
            throw new IllegalArgumentException("Acción de chat no válida.");
        } catch (Exception exception) {
            request.setAttribute("errorChat", exception.getMessage() == null
                    ? "No fue posible completar la acción del chat." : exception.getMessage());
            loadPage(request, response);
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = userId(request);
        String role = role(request);
        try {
            List<ChatConversation> chats = chatDao.findConversations(userId, role);
            request.setAttribute("chats", chats);
            request.setAttribute("propiedadesChat", propertyDao.findPublic("", "", "", null));
            String selected = value(request, "id");
            if (selected.isEmpty() && !chats.isEmpty()) selected = String.valueOf(chats.get(0).getId());
            if (!selected.isEmpty()) {
                int chatId = Integer.parseInt(selected);
                ChatConversation conversation = chatDao.findConversation(chatId, userId, role);
                if (conversation != null) {
                    request.setAttribute("chatSeleccionado", conversation);
                    request.setAttribute("mensajesChat", chatDao.findMessages(chatId, userId));
                }
            }
        } catch (Exception exception) {
            request.setAttribute("chats", Collections.emptyList());
            request.setAttribute("propiedadesChat", Collections.emptyList());
            request.setAttribute("errorChat", "No fue posible cargar el chat de soporte.");
            getServletContext().log("Error cargando chat", exception);
        }
        request.setAttribute("esGestorChat", isManager(role));
        request.setAttribute("tituloPagina", "Chat de soporte");
        request.getRequestDispatcher("/app/chat.jsp").forward(request, response);
    }

    private static boolean isManager(String role) {
        return "INMOBILIARIA".equals(role) || "ADMINISTRADOR".equals(role);
    }

    private static boolean canUseChat(HttpServletRequest request) {
        String role = role(request);
        return "CLIENTE".equals(role) || isManager(role);
    }

    private static void requireRole(String actual, String expected) {
        if (!expected.equals(actual)) throw new IllegalArgumentException("Solo un cliente puede iniciar una conversación.");
    }

    private static int userId(HttpServletRequest request) {
        return ((Number) request.getSession(false).getAttribute("usuarioId")).intValue();
    }

    private static String role(HttpServletRequest request) {
        Object role = request.getSession(false).getAttribute("usuarioRol");
        return role == null ? "" : role.toString();
    }

    private static String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private static int integer(HttpServletRequest request, String name) {
        return Integer.parseInt(value(request, name));
    }

    private static int integerOrZero(HttpServletRequest request, String name) {
        String value = value(request, name);
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private static void redirect(HttpServletResponse response, HttpServletRequest request,
            int chatId, String query) throws IOException {
        response.sendRedirect(request.getContextPath() + "/app/chat?id=" + chatId + "&" + query);
    }
}

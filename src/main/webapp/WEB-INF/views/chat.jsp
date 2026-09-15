<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.ChatConversation,co.edu.uts.inmobiliaria.model.ChatMessage,co.edu.uts.inmobiliaria.model.Property,java.util.List,java.sql.Timestamp,java.time.format.DateTimeFormatter,java.util.Locale" %>
<%!
    private String fechaHoraLegible(Timestamp fecha) {
        if (fecha == null) return "-";
        return DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", new Locale("es", "CO"))
                .format(fecha.toLocalDateTime());
    }
%>
<%
    List<ChatConversation> chats = (List<ChatConversation>) request.getAttribute("chats");
    List<ChatMessage> mensajes = (List<ChatMessage>) request.getAttribute("mensajesChat");
    List<Property> propiedades = (List<Property>) request.getAttribute("propiedadesChat");
    ChatConversation seleccionado = (ChatConversation) request.getAttribute("chatSeleccionado");
    boolean esGestor = Boolean.TRUE.equals(request.getAttribute("esGestorChat"));
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="mb-4"><span class="section-kicker">Soporte privado</span><h1 class="fw-bold mb-1">Chat con Altaltium</h1><p class="text-secondary mb-0"><%= esGestor ? "Atiende las conversaciones de los clientes y ciérralas cuando el caso esté resuelto." : "Comunícate de forma privada con el equipo inmobiliario." %></p></div>
    <% if (request.getAttribute("mensajeChat") != null) { %><div class="alert alert-success"><%= request.getAttribute("mensajeChat") %></div><% } %>
    <% if (request.getAttribute("errorChat") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorChat") %></div><% } %>
    <div class="row g-4 align-items-start">
        <div class="col-lg-4">
            <% if (!esGestor) { %>
            <div class="card operation-card shadow-sm mb-4"><div class="card-body p-4"><div class="form-heading mb-3"><span class="form-heading-icon">✉</span><div><h2 class="h5 fw-bold mb-1">Nuevo chat</h2><p class="small text-secondary mb-0">Tu mensaje será asignado a un inmobiliario disponible.</p></div></div><form action="<%= request.getContextPath() %>/app/chat.jsp" method="post" class="row g-3"><input type="hidden" name="accion" value="crear"><div class="col-12"><label class="form-label" for="asunto">Asunto</label><input class="form-control" id="asunto" name="asunto" maxlength="150" placeholder="Ej. Información sobre una propiedad" required></div><div class="col-12"><label class="form-label" for="idPropiedad">Propiedad relacionada</label><select class="form-select" id="idPropiedad" name="idPropiedad"><option value="">Ninguna / consulta general</option><% if (propiedades != null) { for (Property propiedad : propiedades) { %><option value="<%= propiedad.getId() %>"><%= propiedad.getTitle() %> · <%= propiedad.getCity() %></option><% } } %></select></div><div class="col-12"><label class="form-label" for="mensajeInicial">Mensaje</label><textarea class="form-control" id="mensajeInicial" name="mensaje" rows="4" maxlength="2000" placeholder="Escribe tu consulta..." required></textarea></div><div class="col-12"><button class="btn btn-accent w-100" type="submit">Iniciar conversación</button></div></form></div></div>
            <% } %>
            <div class="card border-0 shadow-sm"><div class="card-body p-3"><div class="d-flex justify-content-between align-items-center mb-3"><h2 class="h5 fw-bold mb-0">Conversaciones</h2><span class="badge text-bg-light"><%= chats == null ? 0 : chats.size() %></span></div><div class="chat-list"><% if (chats != null && !chats.isEmpty()) { for (ChatConversation chat : chats) { %><form action="<%= request.getContextPath() %>/app/chat.jsp" method="post"><input type="hidden" name="accion" value="seleccionar"><input type="hidden" name="idChat" value="<%= chat.getId() %>"><button class="chat-list-item <%= seleccionado != null && seleccionado.getId() == chat.getId() ? "active" : "" %>" type="submit"><span class="chat-list-title"><%= chat.getSubject() %></span><small><%= esGestor ? chat.getClientName() : chat.getAgencyName() %> · <%= chat.getStatus() %></small><small><%= fechaHoraLegible(chat.getCreatedAt()) %></small></button></form><% } } else { %><p class="text-secondary small mb-0">Aún no tienes conversaciones.</p><% } %></div></div></div>
        </div>
        <div class="col-lg-8">
            <% if (seleccionado != null) { %>
            <div class="card chat-card border-0 shadow-sm"><div class="card-body p-4"><div class="d-flex flex-column flex-md-row justify-content-between align-items-md-start gap-3 border-bottom pb-3 mb-3"><div><span class="badge <%= "ABIERTA".equals(seleccionado.getStatus()) ? "text-bg-success" : "text-bg-secondary" %> mb-2"><%= seleccionado.getStatus() %></span><h2 class="h4 fw-bold mb-1"><%= seleccionado.getSubject() %></h2><p class="text-secondary small mb-0"><%= seleccionado.getPropertyTitle() == null ? "Consulta general" : seleccionado.getPropertyTitle() %> · Creado el <%= fechaHoraLegible(seleccionado.getCreatedAt()) %></p></div><% if (esGestor && "ABIERTA".equals(seleccionado.getStatus())) { %><form action="<%= request.getContextPath() %>/app/chat.jsp" method="post"><input type="hidden" name="accion" value="cerrar"><input type="hidden" name="idChat" value="<%= seleccionado.getId() %>"><button class="btn btn-outline-danger" type="submit">Cerrar conversación</button></form><% } %></div><div class="chat-messages"><% if (mensajes != null && !mensajes.isEmpty()) { for (ChatMessage mensaje : mensajes) { %><div class="chat-message <%= mensaje.isFromCurrentUser() ? "mine" : "theirs" %>"><div class="chat-bubble"><strong><%= mensaje.getSenderName() %></strong><p><%= mensaje.getText() %></p><small><%= fechaHoraLegible(mensaje.getSentAt()) %></small></div></div><% } } else { %><p class="text-secondary text-center py-5">No hay mensajes todavía.</p><% } %></div><% if ("ABIERTA".equals(seleccionado.getStatus())) { %><form action="<%= request.getContextPath() %>/app/chat.jsp" method="post" class="chat-reply-form mt-4"><input type="hidden" name="accion" value="mensaje"><input type="hidden" name="idChat" value="<%= seleccionado.getId() %>"><textarea class="form-control" name="mensaje" rows="2" maxlength="2000" placeholder="Escribe una respuesta..." required></textarea><button class="btn btn-accent" type="submit">Enviar mensaje</button></form><% } else { %><div class="alert alert-light border mb-0">Esta conversación está cerrada y ya no admite nuevos mensajes.</div><% } %></div></div>
            <% } else { %><div class="empty-state text-center p-5"><div class="empty-state-icon">✉</div><h2 class="h5">Selecciona una conversación</h2><p class="text-secondary mb-0">Elige un chat de la lista o inicia uno nuevo.</p></div><% } %>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

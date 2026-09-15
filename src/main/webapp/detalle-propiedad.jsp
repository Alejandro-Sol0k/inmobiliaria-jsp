<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.List,java.math.BigDecimal,java.math.RoundingMode,java.net.URLEncoder" %>
<%!
    private String precioLegible(BigDecimal precio) {
        if (precio == null) return "Consultar";
        if (precio.compareTo(BigDecimal.valueOf(1000000)) >= 0) return "$ " + precio.divide(BigDecimal.valueOf(1000000), 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString().replace('.', ',') + " millones";
        if (precio.compareTo(BigDecimal.valueOf(1000)) >= 0) return "$ " + precio.divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString().replace('.', ',') + " mil";
        return "$ " + precio.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
%>
<%
    Property propiedad = (Property) request.getAttribute("propiedad");
    List<String> imagenes = (List<String>) request.getAttribute("imagenes");
    List<String> caracteristicas = (List<String>) request.getAttribute("caracteristicas");
    boolean autenticado = session != null && session.getAttribute("usuarioId") != null;
    String loginParaOperacion = request.getContextPath() + "/auth/login.jsp?redirect="
            + URLEncoder.encode("/propiedad.jsp?id=" + (propiedad == null ? "" : propiedad.getId()), "UTF-8");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <% if (request.getAttribute("errorDetalle") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorDetalle") %></div><% } %>
    <% if (propiedad != null) { %>
    <div class="row g-4 align-items-start"><div class="col-lg-7"><div class="property-gallery shadow-sm"><div class="row g-2"><% if (imagenes != null && !imagenes.isEmpty()) { for (String imagen : imagenes) { %><div class="col-12"><img src="<%= imagen %>" class="d-block w-100" alt="<%= propiedad.getTitle() %>"></div><% } } else { %><div class="col-12"><img src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" class="d-block w-100 detail-fallback" alt="Altaltium Real Estate"></div><% } %></div></div></div><div class="col-lg-5"><div class="card detail-card border-0 shadow-sm"><div class="card-body p-4 p-lg-5"><span class="badge text-bg-light mb-3"><%= propiedad.getOperation() %></span><h1 class="h2 fw-bold"><%= propiedad.getTitle() %></h1><p class="text-secondary"><%= propiedad.getCity() %> · <% if (autenticado) { %><%= propiedad.getAddress() %><% } else { %>dirección exacta disponible al iniciar sesión<% } %></p><div class="gold-line mb-3"></div><p class="display-6 fw-bold text-brand"><%= precioLegible(propiedad.getPrice()) %></p><div class="property-facts"><span><strong><%= propiedad.getBedrooms() %></strong> habitaciones</span><span><strong><%= propiedad.getBathrooms() %></strong> baños</span><span><strong><%= propiedad.getArea() %></strong> m²</span></div><p class="mt-4 text-secondary"><%= propiedad.getDescription() == null ? "Sin descripción disponible." : propiedad.getDescription() %></p><h2 class="h6 fw-bold mt-4">Características</h2><div class="catalog-chips"><% if (caracteristicas != null && !caracteristicas.isEmpty()) { for (String caracteristica : caracteristicas) { %><span><%= caracteristica %></span><% } } else { %><small class="text-secondary">No se han registrado características.</small><% } %></div><div class="d-grid gap-2 mt-4"><% if (autenticado) { %><a class="btn btn-accent" href="<%= request.getContextPath() %>/app/operaciones?propiedad=<%= propiedad.getId() %>#form-cita">Solicitar una visita</a><a class="btn btn-outline-dark" href="<%= request.getContextPath() %>/app/operaciones?propiedad=<%= propiedad.getId() %>#form-solicitud">Radicar solicitud</a><% } else { %><a class="btn btn-accent" href="<%= loginParaOperacion %>">Iniciar sesión para solicitar una visita</a><a class="btn btn-outline-dark" href="<%= loginParaOperacion %>">Iniciar sesión para radicar solicitud</a><% } %></div></div></div></div></div>
    <% } %>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

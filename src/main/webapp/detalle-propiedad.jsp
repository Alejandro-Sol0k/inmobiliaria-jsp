<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.List,java.math.BigDecimal,java.math.RoundingMode" %>
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
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <% if (request.getAttribute("errorDetalle") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorDetalle") %></div><% } %>
    <% if (propiedad != null) { %>
    <div class="row g-4 align-items-start"><div class="col-lg-7"><div id="galeriaPropiedad" class="carousel slide property-gallery shadow-sm" data-bs-ride="carousel"><div class="carousel-inner"><% if (imagenes != null && !imagenes.isEmpty()) { int indice = 0; for (String imagen : imagenes) { %><div class="carousel-item <%= indice++ == 0 ? "active" : "" %>"><img src="<%= imagen %>" class="d-block w-100" alt="<%= propiedad.getTitle() %>"></div><% } } else { %><div class="carousel-item active"><img src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" class="d-block w-100 detail-fallback" alt="Altaltium Real Estate"></div><% } %></div><% if (imagenes != null && imagenes.size() > 1) { %><button class="carousel-control-prev" type="button" data-bs-target="#galeriaPropiedad" data-bs-slide="prev"><span class="carousel-control-prev-icon"></span></button><button class="carousel-control-next" type="button" data-bs-target="#galeriaPropiedad" data-bs-slide="next"><span class="carousel-control-next-icon"></span></button><% } %></div></div><div class="col-lg-5"><div class="card detail-card border-0 shadow-sm"><div class="card-body p-4 p-lg-5"><span class="badge text-bg-light mb-3"><%= propiedad.getOperation() %></span><h1 class="h2 fw-bold"><%= propiedad.getTitle() %></h1><p class="text-secondary"><%= propiedad.getCity() %> · <%= propiedad.getAddress() %></p><div class="gold-line mb-3"></div><p class="display-6 fw-bold text-brand"><%= precioLegible(propiedad.getPrice()) %></p><div class="property-facts"><span><strong><%= propiedad.getBedrooms() %></strong> habitaciones</span><span><strong><%= propiedad.getBathrooms() %></strong> baños</span><span><strong><%= propiedad.getArea() %></strong> m²</span></div><p class="mt-4 text-secondary"><%= propiedad.getDescription() == null ? "Sin descripción disponible." : propiedad.getDescription() %></p><h2 class="h6 fw-bold mt-4">Características</h2><div class="catalog-chips"><% if (caracteristicas != null && !caracteristicas.isEmpty()) { for (String caracteristica : caracteristicas) { %><span><%= caracteristica %></span><% } } else { %><small class="text-secondary">No se han registrado características.</small><% } %></div><div class="d-grid gap-2 mt-4"><a class="btn btn-accent" href="<%= request.getContextPath() %>/app/operaciones?propiedad=<%= propiedad.getId() %>#form-cita">Solicitar una visita</a><a class="btn btn-outline-dark" href="<%= request.getContextPath() %>/app/operaciones?propiedad=<%= propiedad.getId() %>#form-solicitud">Radicar solicitud</a></div></div></div></div></div>
    <% } %>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

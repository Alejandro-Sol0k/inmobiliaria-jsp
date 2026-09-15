<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.List,java.math.BigDecimal,java.math.RoundingMode" %>
<%!
    private String precioLegible(BigDecimal precio) {
        if (precio == null) return "Consultar";
        if (precio.compareTo(BigDecimal.valueOf(1000000)) >= 0) {
            return "$ " + precio.divide(BigDecimal.valueOf(1000000), 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString().replace('.', ',') + " millones";
        }
        if (precio.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return "$ " + precio.divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString().replace('.', ',') + " mil";
        }
        return "$ " + precio.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
%>
<%
    List<Property> favoritos = (List<Property>) request.getAttribute("favoritos");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="mb-4"><span class="section-kicker">Cuenta personal</span><h1 class="fw-bold mb-1">Mis favoritos</h1><p class="text-secondary mb-0">Guarda las propiedades que quieres revisar después.</p></div>
    <% if (request.getAttribute("mensajeFavoritos") != null) { %><div class="alert alert-success"><%= request.getAttribute("mensajeFavoritos") %></div><% } %>
    <% if (request.getAttribute("errorFavoritos") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorFavoritos") %></div><% } %>
    <div class="row g-4">
        <% if (favoritos != null && !favoritos.isEmpty()) { for (Property propiedad : favoritos) { %>
            <div class="col-md-6 col-xl-4"><article class="card property-card h-100 border-0 shadow-sm overflow-hidden"><form action="<%= request.getContextPath() %>/propiedad.jsp" method="post" class="property-detail-form"><input type="hidden" name="id" value="<%= propiedad.getId() %>"><button class="property-image-link" type="submit" aria-label="Ver detalle de <%= propiedad.getTitle() %>"><div class="ratio ratio-16x9 bg-brand"><img src="<%= propiedad.getImageUrl() == null ? request.getContextPath() + "/assets/images/altaltium-logo.png" : propiedad.getImageUrl() %>" class="object-fit-cover" alt="<%= propiedad.getTitle() %>"></div></button></form><div class="card-body d-flex flex-column"><span class="badge text-bg-light align-self-start mb-2"><%= propiedad.getOperation() %></span><h2 class="h5"><%= propiedad.getTitle() %></h2><p class="text-secondary mb-2"><%= propiedad.getCity() %> · <%= propiedad.getBedrooms() %> habitaciones · <%= propiedad.getBathrooms() %> baños</p><p class="fw-bold text-brand"><%= precioLegible(propiedad.getPrice()) %></p><div class="d-grid gap-2 mt-auto"><form action="<%= request.getContextPath() %>/propiedad.jsp" method="post"><input type="hidden" name="id" value="<%= propiedad.getId() %>"><button class="btn btn-sm btn-accent w-100" type="submit">Ver detalle</button></form><form action="<%= request.getContextPath() %>/app/favoritos" method="post"><input type="hidden" name="accion" value="quitar"><input type="hidden" name="idPropiedad" value="<%= propiedad.getId() %>"><button class="btn btn-sm btn-outline-dark w-100" type="submit">Quitar de favoritos</button></form></div></div></article></div>
        <% } } else { %><div class="col-12"><div class="empty-state text-center p-5"><div class="empty-state-icon">♡</div><h2 class="h5">Todavía no tienes favoritos</h2><p class="text-secondary">Explora el catálogo y guarda las propiedades que te interesen.</p><a class="btn btn-accent" href="<%= request.getContextPath() %>/propiedades.jsp">Explorar propiedades</a></div></div><% } %>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

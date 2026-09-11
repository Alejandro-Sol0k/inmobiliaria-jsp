<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.List" %>
<%
    List<Property> propiedades = (List<Property>) request.getAttribute("propiedades");
    String errorCatalogo = (String) request.getAttribute("errorCatalogo");
    String filtroCiudad = (String) request.getAttribute("filtroCiudad");
    String filtroTipo = (String) request.getAttribute("filtroTipo");
    String filtroOperacion = (String) request.getAttribute("filtroOperacion");
    String filtroPrecioMax = (String) request.getAttribute("filtroPrecioMax");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="mb-4"><span class="text-uppercase small text-brand fw-semibold">Catálogo</span><h1 class="fw-bold mb-1">Encuentra tu propiedad</h1><p class="text-secondary mb-0">Filtra las publicaciones disponibles y encuentra la opción adecuada.</p></div>
    <form action="<%= request.getContextPath() %>/propiedades" method="get" class="card border-0 shadow-sm p-3 p-lg-4 mb-5">
        <div class="row g-3 align-items-end">
            <div class="col-md-3"><label class="form-label" for="ciudad">Ciudad</label><input class="form-control" id="ciudad" name="ciudad" value="<%= filtroCiudad == null ? "" : filtroCiudad %>" placeholder="Bucaramanga"></div>
            <div class="col-md-3"><label class="form-label" for="tipo">Tipo</label><select class="form-select" id="tipo" name="tipo"><option value="">Todos</option><option <%= "Casa".equals(filtroTipo) ? "selected" : "" %>>Casa</option><option <%= "Apartamento".equals(filtroTipo) ? "selected" : "" %>>Apartamento</option><option <%= "Local".equals(filtroTipo) ? "selected" : "" %>>Local</option><option <%= "Oficina".equals(filtroTipo) ? "selected" : "" %>>Oficina</option><option <%= "Terreno".equals(filtroTipo) ? "selected" : "" %>>Terreno</option></select></div>
            <div class="col-md-2"><label class="form-label" for="operacion">Operación</label><select class="form-select" id="operacion" name="operacion"><option value="">Todas</option><option value="VENTA" <%= "VENTA".equals(filtroOperacion) ? "selected" : "" %>>Venta</option><option value="ARRIENDO" <%= "ARRIENDO".equals(filtroOperacion) ? "selected" : "" %>>Arriendo</option></select></div>
            <div class="col-md-2"><label class="form-label" for="precioMax">Precio máximo</label><input class="form-control" id="precioMax" name="precioMax" type="number" min="0" step="100000" value="<%= filtroPrecioMax == null ? "" : filtroPrecioMax %>"></div>
            <div class="col-md-2"><button class="btn btn-accent w-100" type="submit">Filtrar</button></div>
        </div>
    </form>
    <% if (errorCatalogo != null) { %><div class="alert alert-danger"><%= errorCatalogo %></div><% } %>
    <div class="d-flex justify-content-between align-items-center mb-3"><h2 class="h4 mb-0">Propiedades disponibles</h2><span class="text-secondary small"><%= propiedades == null ? 0 : propiedades.size() %> resultados</span></div>
    <div class="row g-4">
        <% if (propiedades != null && !propiedades.isEmpty()) { for (Property propiedad : propiedades) { %>
            <div class="col-md-6 col-xl-4"><article class="card property-card h-100 border-0 shadow-sm overflow-hidden"><div class="ratio ratio-16x9 bg-brand"><img src="<%= propiedad.getImageUrl() == null ? "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=900&q=80" : propiedad.getImageUrl() %>" class="object-fit-cover" alt="<%= propiedad.getTitle() %>"></div><div class="card-body"><span class="badge text-bg-light mb-2"><%= propiedad.getOperation() %></span><h3 class="h5"><%= propiedad.getTitle() %></h3><p class="text-secondary mb-2"><%= propiedad.getCity() %> · <%= propiedad.getBedrooms() %> habitaciones · <%= propiedad.getBathrooms() %> baños</p><p class="fw-bold text-brand mb-0">$ <%= propiedad.getPrice().toPlainString() %> · <%= propiedad.getArea().toPlainString() %> m²</p></div></article></div>
        <% } } else if (errorCatalogo == null) { %><div class="col-12"><div class="alert alert-light border">No encontramos propiedades con esos filtros.</div></div><% } %>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.List,java.math.BigDecimal,java.math.RoundingMode" %>
<%!
    private String precioLegible(BigDecimal precio) {
        if (precio == null) {
            return "Consultar";
        }
        BigDecimal millon = BigDecimal.valueOf(1000000);
        if (precio.compareTo(millon) >= 0) {
            String valor = precio.divide(millon, 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
            return "$ " + valor.replace('.', ',') + " millones";
        }
        BigDecimal mil = BigDecimal.valueOf(1000);
        if (precio.compareTo(mil) >= 0) {
            String valor = precio.divide(mil, 1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
            return "$ " + valor.replace('.', ',') + " mil";
        }
        return "$ " + precio.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
%>
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
    <div class="mb-4"><span class="section-kicker">Catálogo</span><h1 class="fw-bold mb-1">Encuentra tu propiedad</h1><p class="text-secondary mb-0">Filtra las publicaciones disponibles y encuentra la opción adecuada.</p></div>
    <form action="<%= request.getContextPath() %>/propiedades" method="get" class="card filter-panel border-0 shadow-sm p-3 p-lg-4 mb-5">
        <div class="d-flex align-items-center gap-2 mb-3"><span class="filter-icon">⌕</span><div><h2 class="h6 fw-bold mb-0">Buscar propiedades</h2><small class="text-secondary">Puedes combinar varios filtros.</small></div></div>
        <div class="row g-3 align-items-end">
            <div class="col-md-3"><label class="form-label" for="ciudad">Ciudad</label><input class="form-control" id="ciudad" name="ciudad" value="<%= filtroCiudad == null ? "" : filtroCiudad %>" placeholder="Bucaramanga"></div>
            <div class="col-md-3"><label class="form-label" for="tipo">Tipo</label><select class="form-select" id="tipo" name="tipo"><option value="">Todos</option><option <%= "Casa".equals(filtroTipo) ? "selected" : "" %>>Casa</option><option <%= "Apartamento".equals(filtroTipo) ? "selected" : "" %>>Apartamento</option><option <%= "Local".equals(filtroTipo) ? "selected" : "" %>>Local</option><option <%= "Oficina".equals(filtroTipo) ? "selected" : "" %>>Oficina</option><option <%= "Terreno".equals(filtroTipo) ? "selected" : "" %>>Terreno</option></select></div>
            <div class="col-md-2"><label class="form-label" for="operacion">Operación</label><select class="form-select" id="operacion" name="operacion"><option value="">Todas</option><option value="VENTA" <%= "VENTA".equals(filtroOperacion) ? "selected" : "" %>>Venta</option><option value="ARRIENDO" <%= "ARRIENDO".equals(filtroOperacion) ? "selected" : "" %>>Arriendo</option></select></div>
            <div class="col-md-2"><label class="form-label" for="precioMax">Precio máximo</label><div class="input-group money-field"><span class="input-group-text">$</span><input class="form-control" id="precioMax" name="precioMax" inputmode="numeric" value="<%= filtroPrecioMax == null ? "" : filtroPrecioMax %>" placeholder="Ej. 500.000.000"></div></div>
            <div class="col-md-2 d-flex gap-2"><button class="btn btn-accent flex-grow-1" type="submit">Filtrar</button><a class="btn btn-outline-secondary btn-clear-filter" href="<%= request.getContextPath() %>/propiedades" title="Quitar filtros">Quitar</a></div>
        </div>
    </form>
    <% if (errorCatalogo != null) { %><div class="alert alert-danger"><%= errorCatalogo %></div><% } %>
    <div class="d-flex justify-content-between align-items-center mb-3"><h2 class="h4 mb-0">Propiedades disponibles</h2><span class="text-secondary small"><%= propiedades == null ? 0 : propiedades.size() %> resultados</span></div>
    <div class="row g-4">
        <% if (propiedades != null && !propiedades.isEmpty()) { for (Property propiedad : propiedades) { %>
            <div class="col-md-6 col-xl-4"><article class="card property-card h-100 border-0 shadow-sm overflow-hidden"><a class="property-image-link" href="<%= request.getContextPath() %>/propiedad?id=<%= propiedad.getId() %>" aria-label="Ver detalle de <%= propiedad.getTitle() %>"><div class="ratio ratio-16x9 bg-brand"><img src="<%= propiedad.getImageUrl() == null ? "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=900&q=80" : propiedad.getImageUrl() %>" class="object-fit-cover" alt="<%= propiedad.getTitle() %>"></div></a><div class="card-body"><span class="badge text-bg-light mb-2"><%= propiedad.getOperation() %></span><h3 class="h5"><%= propiedad.getTitle() %></h3><p class="text-secondary mb-2"><%= propiedad.getCity() %> · <%= propiedad.getBedrooms() %> habitaciones · <%= propiedad.getBathrooms() %> baños</p><p class="fw-bold text-brand mb-3"><%= precioLegible(propiedad.getPrice()) %> · <%= propiedad.getArea().toPlainString() %> m²</p><a class="btn btn-sm btn-accent w-100 mb-2" href="<%= request.getContextPath() %>/propiedad?id=<%= propiedad.getId() %>">Ver detalle</a><% if (session.getAttribute("usuarioId") != null) { %><form action="<%= request.getContextPath() %>/app/favoritos" method="post"><input type="hidden" name="accion" value="agregar"><input type="hidden" name="idPropiedad" value="<%= propiedad.getId() %>"><button class="btn btn-sm btn-outline-dark w-100" type="submit">♡ Agregar a favoritos</button></form><% } %></div></article></div>
        <% } } else if (errorCatalogo == null) { %><div class="col-12"><div class="alert alert-light border">No encontramos propiedades con esos filtros.</div></div><% } %>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

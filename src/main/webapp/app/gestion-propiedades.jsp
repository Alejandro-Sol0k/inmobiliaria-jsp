<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.List,java.math.BigDecimal,java.math.RoundingMode,java.text.NumberFormat,java.util.Locale" %>
<%!
    private String precioCompleto(BigDecimal precio) {
        NumberFormat formato = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        formato.setMaximumFractionDigits(0);
        formato.setMinimumFractionDigits(0);
        return formato.format(precio.setScale(0, RoundingMode.HALF_UP));
    }
%>
<%
    List<Property> propiedades = (List<Property>) request.getAttribute("propiedades");
    String errorGestion = (String) request.getAttribute("errorGestion");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4"><div><span class="section-kicker">Sprint 2 · Administración</span><h1 class="fw-bold mb-1">Gestión de propiedades</h1><p class="text-secondary mb-0">Publica y administra el inventario de Altaltium Real Estate.</p></div><a class="btn btn-outline-dark" href="<%= request.getContextPath() %>/propiedades">Ver catálogo público</a></div>
    <% if ("ok".equals(request.getParameter("creada"))) { %><div class="alert alert-success">La propiedad fue creada correctamente.</div><% } %>
    <% if ("ok".equals(request.getParameter("desactivada"))) { %><div class="alert alert-success">La propiedad fue dada de baja lógicamente.</div><% } %>
    <% if (errorGestion != null) { %><div class="alert alert-danger"><%= errorGestion %></div><% } %>
    <div class="row g-4 align-items-start">
        <div class="col-lg-5">
            <div class="card property-form-card shadow-sm"><div class="card-body p-4 p-xl-5">
                <div class="form-heading"><span class="form-heading-icon">✦</span><div><h2 class="h5 fw-bold mb-1">Nueva publicación</h2><p class="text-secondary small mb-0">Completa la información de la propiedad.</p></div></div><div class="gold-line mb-4"></div>
                <form action="<%= request.getContextPath() %>/app/gestion-propiedades" method="post" enctype="multipart/form-data" class="row g-3 property-form">
                    <div class="col-12"><label class="form-label" for="titulo">Título</label><input class="form-control" id="titulo" name="titulo" required></div>
                    <div class="col-md-6"><label class="form-label" for="ciudad">Ciudad</label><select class="form-select" id="ciudad" name="ciudad" required><option value="">Selecciona</option><option>Bucaramanga</option><option>Floridablanca</option><option>Girón</option><option>Piedecuesta</option></select></div>
                    <div class="col-md-6"><label class="form-label" for="tipo">Tipo</label><select class="form-select" id="tipo" name="tipo" required><option value="">Selecciona</option><option>Casa</option><option>Apartamento</option><option>Local</option><option>Oficina</option><option>Terreno</option></select></div>
                    <div class="col-md-6"><label class="form-label" for="operacion">Operación</label><select class="form-select" id="operacion" name="operacion" required><option value="VENTA">Venta</option><option value="ARRIENDO">Arriendo</option></select></div>
                    <div class="col-md-6"><label class="form-label" for="matricula">Matrícula</label><input class="form-control" id="matricula" name="matricula" required></div>
                    <div class="col-md-6"><label class="form-label" for="precioVisible">Precio</label><div class="input-group money-field"><span class="input-group-text">$</span><input class="form-control" id="precioVisible" data-money-input="precio" inputmode="numeric" required placeholder="Ej. 480.000.000"><input type="hidden" name="precio" data-money-value="precio"></div><div class="form-text">Escribe solo números; se agregan los puntos automáticamente.</div></div>
                    <div class="col-md-6"><label class="form-label" for="area">Área m²</label><input class="form-control" id="area" name="area" type="number" min="1" step="0.01" required></div>
                    <div class="col-md-4"><label class="form-label" for="habitaciones">Habitaciones</label><input class="form-control" id="habitaciones" name="habitaciones" type="number" min="0" required></div>
                    <div class="col-md-4"><label class="form-label" for="banos">Baños</label><input class="form-control" id="banos" name="banos" type="number" min="0" required></div>
                    <div class="col-md-4"><label class="form-label" for="direccion">Dirección</label><input class="form-control" id="direccion" name="direccion" required></div>
                    <div class="col-12"><div class="media-upload-box"><div class="d-flex align-items-center gap-2 mb-2"><span class="media-upload-icon">▧</span><div><label class="form-label mb-0">Imagen principal</label><div class="small text-secondary">Elige una de las dos opciones</div></div></div><input class="form-control mb-2" name="imagenUrl" type="url" placeholder="Opción 1 · URL pública: https://..."><div class="upload-divider"><span>o</span></div><input class="form-control" name="imagenArchivo" type="file" accept="image/jpeg,image/png,image/gif,image/webp"><div class="form-text">Opción 2 · carga JPG, PNG, GIF o WEBP (máximo 5 MB). Si eliges ambas, se usará el archivo.</div></div></div>
                    <div class="col-12"><label class="form-label d-block">Características</label><div class="feature-grid"><label class="feature-check"><input class="form-check-input" type="checkbox" name="caracteristicas" value="Piscina"><span>Piscina</span></label><label class="feature-check"><input class="form-check-input" type="checkbox" name="caracteristicas" value="Parqueadero"><span>Parqueadero</span></label><label class="feature-check"><input class="form-check-input" type="checkbox" name="caracteristicas" value="Ascensor"><span>Ascensor</span></label><label class="feature-check"><input class="form-check-input" type="checkbox" name="caracteristicas" value="Gimnasio"><span>Gimnasio</span></label><label class="feature-check"><input class="form-check-input" type="checkbox" name="caracteristicas" value="Vigilancia"><span>Vigilancia</span></label></div></div>
                    <div class="col-12"><label class="form-label" for="descripcion">Descripción</label><textarea class="form-control" id="descripcion" name="descripcion" rows="3"></textarea></div>
                    <div class="col-12"><button class="btn btn-accent w-100 py-3" type="submit">Publicar propiedad</button></div>
                </form>
            </div></div>
        </div>
        <div class="col-lg-7"><div class="card border-0 shadow-sm"><div class="card-body p-4"><div class="d-flex justify-content-between align-items-center mb-3"><h2 class="h5 fw-bold mb-0">Publicaciones activas</h2><span class="badge text-bg-light"><%= propiedades == null ? 0 : propiedades.size() %> propiedades</span></div><div class="table-responsive"><table class="table align-middle"><thead><tr><th>Propiedad</th><th>Ubicación</th><th>Operación</th><th>Precio</th><th></th></tr></thead><tbody><% if (propiedades != null && !propiedades.isEmpty()) { for (Property propiedad : propiedades) { %><tr><td><strong><%= propiedad.getTitle() %></strong><br><small class="text-secondary"><%= propiedad.getType() %></small></td><td><%= propiedad.getCity() %></td><td><span class="badge text-bg-light"><%= propiedad.getOperation() %></span></td><td class="text-nowrap">$ <%= precioCompleto(propiedad.getPrice()) %></td><td><form action="<%= request.getContextPath() %>/app/gestion-propiedades" method="post"><input type="hidden" name="accion" value="desactivar"><input type="hidden" name="idPropiedad" value="<%= propiedad.getId() %>"><button class="btn btn-sm btn-outline-danger" type="submit">Dar de baja</button></form></td></tr><% } } else { %><tr><td colspan="5" class="text-secondary">Todavía no hay publicaciones activas.</td></tr><% } %></tbody></table></div></div></div></div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

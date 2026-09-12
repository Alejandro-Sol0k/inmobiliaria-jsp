<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Property,java.util.Collections,java.util.List" %>
<%!
    private String urlDetalle(String contextPath, int propiedadId) {
        return contextPath + "/propiedad?id=" + propiedadId;
    }
%>
<%
    request.setAttribute("tituloPagina", "Inicio");
    List<Property> propiedadesDestacadas = (List<Property>) request.getAttribute("propiedadesDestacadas");
    if (propiedadesDestacadas == null) {
        propiedadesDestacadas = Collections.emptyList();
    }
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>

<main>
    <section class="hero">
        <div class="container py-5">
            <div class="row align-items-center g-5">
                <div class="col-lg-7">
                    <span class="badge rounded-pill text-bg-warning mb-3">Encuentra tu proximo espacio</span>
                    <h1 class="display-4 fw-bold">Propiedades que se sienten como hogar.</h1>
                    <p class="lead mt-3 mb-4">Explora inmuebles seleccionados en Bucaramanga y su área metropolitana con el respaldo de Altaltium Real Estate.</p>
                    <a class="btn btn-accent btn-lg px-4" href="<%= request.getContextPath() %>/propiedades">Explorar propiedades</a>
                </div>
                <div class="col-lg-5">
                    <div class="text-center mb-4"><img class="hero-logo" src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" alt="Altaltium Real Estate"></div>
                    <div class="hero-card rounded-4 p-4 shadow-lg">
                        <p class="text-uppercase small fw-semibold mb-3">Busqueda rapida</p>
                        <form action="<%= request.getContextPath() %>/propiedades" method="get" class="row g-3">
                            <div class="col-12"><label class="form-label" for="busquedaCiudad">Ciudad</label><input class="form-control" id="busquedaCiudad" name="ciudad" placeholder="Ej. Bucaramanga"></div>
                            <div class="col-6"><label class="form-label" for="busquedaOperacion">Operacion</label><select class="form-select" id="busquedaOperacion" name="operacion"><option value="">Todas</option><option value="VENTA">Venta</option><option value="ARRIENDO">Arriendo</option></select></div>
                            <div class="col-6"><label class="form-label" for="busquedaTipo">Tipo</label><select class="form-select" id="busquedaTipo" name="tipo"><option value="">Todos</option><option value="CASA">Casa</option><option value="APARTAMENTO">Apartamento</option><option value="OFICINA">Oficina</option><option value="TERRENO">Terreno</option></select></div>
                            <div class="col-12"><button class="btn btn-light w-100" type="submit">Buscar opciones</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="propiedades" class="container py-5">
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4">
            <div><span class="text-uppercase small text-brand fw-semibold">Catalogo inicial</span><h2 class="fw-bold mb-0">Propiedades destacadas</h2></div>
            <span class="text-secondary">Selecciona una imagen para consultar los detalles.</span>
        </div>
        <div class="row g-4">
            <% if (propiedadesDestacadas.isEmpty()) { %>
                <div class="col-12"><div class="alert alert-light border">No hay propiedades activas disponibles en este momento.</div></div>
            <% } else {
                int limite = Math.min(3, propiedadesDestacadas.size());
                for (int indice = 0; indice < limite; indice++) {
                    Property propiedad = propiedadesDestacadas.get(indice);
                    String imagen = propiedad.getImageUrl();
                    if (imagen == null || imagen.trim().isEmpty()) {
                        imagen = request.getContextPath() + "/assets/images/altaltium-logo.png";
                    }
                    String enlaceDetalle = urlDetalle(request.getContextPath(), propiedad.getId());
            %>
                <div class="col-md-4">
                    <article class="card property-card h-100 border-0 shadow-sm overflow-hidden">
                        <a class="property-image-link" href="<%= enlaceDetalle %>" aria-label="Ver detalle de <%= propiedad.getTitle() %>">
                            <div class="ratio ratio-16x9 bg-brand"><img src="<%= imagen %>" class="object-fit-cover" alt="Imagen de <%= propiedad.getTitle() %>"></div>
                        </a>
                        <div class="card-body d-flex flex-column">
                            <span class="badge text-bg-light align-self-start mb-2"><%= propiedad.getOperation() %></span>
                            <h3 class="h5"><%= propiedad.getTitle() %></h3>
                            <p class="text-secondary mb-3"><%= propiedad.getCity() %> · <%= propiedad.getBedrooms() %> habitaciones · <%= propiedad.getBathrooms() %> baños</p>
                            <a class="btn btn-accent btn-sm mt-auto" href="<%= enlaceDetalle %>">Ver detalle</a>
                        </div>
                    </article>
                </div>
            <%   }
               }
            %>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>

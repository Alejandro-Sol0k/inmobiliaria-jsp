<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("tituloPagina", "Inicio");
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
                    <p class="lead mt-3 mb-4">Explora inmuebles seleccionados en Bucaramanga y su area metropolitana con el respaldo de Inmobiliaria UTS.</p>
                    <a class="btn btn-accent btn-lg px-4" href="#propiedades">Explorar propiedades</a>
                </div>
                <div class="col-lg-5">
                    <div class="hero-card rounded-4 p-4 shadow-lg">
                        <p class="text-uppercase small fw-semibold mb-3">Busqueda rapida</p>
                        <form action="<%= request.getContextPath() %>/index.jsp#propiedades" method="get" class="row g-3">
                            <div class="col-12"><label class="form-label">Ciudad</label><input class="form-control" name="ciudad" placeholder="Ej. Bucaramanga"></div>
                            <div class="col-6"><label class="form-label">Operacion</label><select class="form-select" name="operacion"><option value="">Todas</option><option>Venta</option><option>Arriendo</option></select></div>
                            <div class="col-6"><label class="form-label">Tipo</label><select class="form-select" name="tipo"><option value="">Todos</option><option>Casa</option><option>Apartamento</option><option>Oficina</option></select></div>
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
            <span class="text-secondary">Primer sprint: estructura lista para conectar el catalogo a MySQL.</span>
        </div>
        <div class="row g-4">
            <div class="col-md-4"><article class="card property-card h-100 border-0 shadow-sm overflow-hidden"><div class="ratio ratio-16x9 bg-brand"><img src="https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=900&q=80" class="object-fit-cover" alt="Casa moderna"></div><div class="card-body"><span class="badge text-bg-light mb-2">Venta</span><h3 class="h5">Casa Brisas del Rio</h3><p class="text-secondary mb-0">Bucaramanga · 3 habitaciones · 2 banos</p></div></article></div>
            <div class="col-md-4"><article class="card property-card h-100 border-0 shadow-sm overflow-hidden"><div class="ratio ratio-16x9 bg-brand"><img src="https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=900&q=80" class="object-fit-cover" alt="Apartamento iluminado"></div><div class="card-body"><span class="badge text-bg-light mb-2">Arriendo</span><h3 class="h5">Apartamento La Riviera</h3><p class="text-secondary mb-0">Floridablanca · 2 habitaciones · Parqueadero</p></div></article></div>
            <div class="col-md-4"><article class="card property-card h-100 border-0 shadow-sm overflow-hidden"><div class="ratio ratio-16x9 bg-brand"><img src="https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=900&q=80" class="object-fit-cover" alt="Oficina moderna"></div><div class="card-body"><span class="badge text-bg-light mb-2">Arriendo</span><h3 class="h5">Oficina Cabecera</h3><p class="text-secondary mb-0">Bucaramanga · 85 m² · Ascensor</p></div></article></div>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/jspf/pie.jspf" %>

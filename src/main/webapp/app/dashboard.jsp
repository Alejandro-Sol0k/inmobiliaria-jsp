<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("tituloPagina", "Panel");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <section class="dashboard-hero p-4 p-lg-5 mb-5 shadow-sm">
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-4">
            <div><span class="section-kicker">Área privada</span><h1 class="display-6 fw-bold mt-2 mb-2">Hola, <%= session.getAttribute("usuarioCorreo") %></h1><div class="gold-line mb-3"></div><p class="mb-0 text-white-50">Rol actual: <strong class="text-white"><%= session.getAttribute("usuarioRol") %></strong></p></div>
            <a class="btn btn-accent" href="<%= request.getContextPath() %>/propiedades">Explorar catálogo</a>
        </div>
    </section>
    <div class="row g-4">
        <div class="col-md-6 col-xl-3"><div class="card dashboard-card soft-panel h-100"><div class="card-body p-4"><span class="text-accent fs-4">◆</span><h2 class="h5 mt-3">Mi perfil</h2><p class="text-secondary">Actualiza tus datos personales para agilizar tus trámites.</p><a class="btn btn-sm btn-accent" href="<%= request.getContextPath() %>/app/perfil">Editar perfil</a></div></div></div>
        <div class="col-md-6 col-xl-3"><div class="card dashboard-card soft-panel h-100"><div class="card-body p-4"><span class="text-accent fs-4">⌂</span><h2 class="h5 mt-3">Propiedades</h2><p class="text-secondary">Consulta el catálogo y encuentra oportunidades disponibles.</p><a class="btn btn-sm btn-accent" href="<%= request.getContextPath() %>/propiedades">Ver propiedades</a></div></div></div>
        <% if ("INMOBILIARIA".equals(session.getAttribute("usuarioRol")) || "ADMINISTRADOR".equals(session.getAttribute("usuarioRol"))) { %>
            <div class="col-md-6 col-xl-3"><div class="card dashboard-card soft-panel h-100"><div class="card-body p-4"><span class="text-accent fs-4">✦</span><h2 class="h5 mt-3">Gestión inmobiliaria</h2><p class="text-secondary">Publica propiedades y administra el inventario disponible.</p><a class="btn btn-sm btn-accent" href="<%= request.getContextPath() %>/app/gestion-propiedades">Gestionar</a></div></div></div>
        <% } %>
        <div class="col-md-6 col-xl-3"><div class="card dashboard-card soft-panel h-100"><div class="card-body p-4"><span class="text-accent fs-4">◷</span><h2 class="h5 mt-3">Citas y solicitudes</h2><p class="text-secondary">Agenda visitas y consulta el avance de tus trámites.</p><span class="badge text-bg-light">Sprint 3</span></div></div></div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

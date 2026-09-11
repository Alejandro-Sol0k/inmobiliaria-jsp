<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.Profile" %>
<%
    Profile perfil = (Profile) request.getAttribute("perfil");
    String nombres = perfil == null ? "" : perfil.getNames();
    String apellidos = perfil == null ? "" : perfil.getLastNames();
    String documento = perfil == null ? "" : perfil.getDocument();
    String telefono = perfil == null || perfil.getPhone() == null ? "" : perfil.getPhone();
    String direccion = perfil == null || perfil.getAddress() == null ? "" : perfil.getAddress();
    String fotoUrl = perfil == null || perfil.getPhotoUrl() == null ? "" : perfil.getPhotoUrl();
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-xl-9">
            <div class="card soft-panel shadow-sm overflow-hidden">
                <div class="row g-0">
                    <div class="col-lg-4 profile-brand-panel p-4 p-lg-5 text-white">
                        <img class="auth-logo mb-4" src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" alt="Altaltium Real Estate">
                        <span class="section-kicker">Cuenta personal</span>
                        <h1 class="h2 fw-bold mt-2">Mi perfil</h1>
                        <p class="text-white-50">Mantén actualizada tu información para gestionar visitas, favoritos y solicitudes.</p>
                    </div>
                    <div class="col-lg-8 p-4 p-lg-5">
                        <% if ("ok".equals(request.getParameter("actualizado"))) { %><div class="alert alert-success">Tu perfil fue actualizado correctamente.</div><% } %>
                        <% if (request.getAttribute("errorPerfil") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorPerfil") %></div><% } %>
                        <form action="<%= request.getContextPath() %>/app/perfil" method="post" class="row g-3">
                            <div class="col-md-6"><label class="form-label">Nombres</label><input class="form-control" name="nombres" value="<%= nombres %>" required></div>
                            <div class="col-md-6"><label class="form-label">Apellidos</label><input class="form-control" name="apellidos" value="<%= apellidos %>" required></div>
                            <div class="col-md-6"><label class="form-label">Documento</label><input class="form-control" name="documento" value="<%= documento %>" required></div>
                            <div class="col-md-6"><label class="form-label">Teléfono</label><input class="form-control" name="telefono" value="<%= telefono %>"></div>
                            <div class="col-12"><label class="form-label">Dirección</label><input class="form-control" name="direccion" value="<%= direccion %>"></div>
                            <div class="col-12"><label class="form-label">Foto de perfil (URL)</label><input class="form-control" type="url" name="fotoUrl" value="<%= fotoUrl %>" placeholder="https://..."></div>
                            <div class="col-12 d-flex justify-content-end gap-2 mt-4"><a class="btn btn-outline-dark" href="<%= request.getContextPath() %>/app/dashboard.jsp">Cancelar</a><button class="btn btn-accent" type="submit">Guardar cambios</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

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
    String fotoMostrada = fotoUrl.isEmpty() ? request.getContextPath() + "/assets/images/profile-default.png" : fotoUrl;
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-xl-9">
            <div class="card soft-panel shadow-sm overflow-hidden">
                <div class="row g-0">
                    <div class="col-lg-4 profile-brand-panel p-4 p-lg-5 text-white">
                        <img class="profile-preview mb-3" src="<%= fotoMostrada %>" alt="Foto de perfil">
                        <img class="auth-logo mb-4" src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" alt="Altaltium Real Estate">
                        <span class="section-kicker">Cuenta personal</span>
                        <h1 class="h2 fw-bold mt-2">Mi perfil</h1>
                        <p class="text-white-50">Mantén actualizada tu información para gestionar visitas, favoritos y solicitudes.</p>
                    </div>
                    <div class="col-lg-8 p-4 p-lg-5">
                        <% if (request.getAttribute("mensajePerfil") != null) { %><div class="alert alert-success"><%= request.getAttribute("mensajePerfil") %></div><% } %>
                        <% if (request.getAttribute("errorPerfil") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorPerfil") %></div><% } %>
                        <form action="<%= request.getContextPath() %>/app/perfil.jsp" method="post" enctype="multipart/form-data" class="row g-3 profile-form">
                            <div class="col-md-6"><label class="form-label" for="nombres">Nombres</label><input class="form-control" id="nombres" name="nombres" value="<%= nombres %>" placeholder="Ej. Joel" required></div>
                            <div class="col-md-6"><label class="form-label" for="apellidos">Apellidos</label><input class="form-control" id="apellidos" name="apellidos" value="<%= apellidos %>" placeholder="Ej. Contreras" required></div>
                            <div class="col-md-6"><label class="form-label" for="documento">Documento</label><input class="form-control" id="documento" name="documento" value="<%= documento %>" placeholder="Ej. 1098765432" required></div>
                            <div class="col-md-6"><label class="form-label" for="telefono">Teléfono</label><input class="form-control" id="telefono" name="telefono" value="<%= telefono %>" placeholder="Ej. 3001234567"></div>
                            <div class="col-12"><label class="form-label" for="direccion">Dirección</label><input class="form-control" id="direccion" name="direccion" value="<%= direccion %>" placeholder="Ej. Carrera 27 # 9-50"></div>
                            <div class="col-12"><div class="media-upload-box"><div class="d-flex align-items-center gap-2 mb-2"><img class="mini-avatar" src="<%= fotoMostrada %>" alt="Avatar actual"><div><label class="form-label mb-0">Foto de perfil</label><div class="small text-secondary">Se mostrará en el header.</div></div></div><input class="form-control mb-2" type="text" inputmode="url" name="fotoUrl" value="<%= fotoUrl %>" placeholder="Opción 1 · URL pública: https://..."><div class="upload-divider"><span>o</span></div><input class="form-control" type="file" name="fotoArchivo" accept="image/jpeg,image/png,image/gif,image/webp"><div class="form-text">Opción 2 · carga JPG, PNG, GIF o WEBP (máximo 5 MB). Si no eliges ninguna, se usará el avatar predeterminado.</div></div></div>
                            <div class="col-12 d-flex justify-content-end gap-2 mt-4"><a class="btn btn-outline-dark" href="<%= request.getContextPath() %>/app/dashboard.jsp">Cancelar</a><button class="btn btn-accent" type="submit">Guardar cambios</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

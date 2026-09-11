<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("tituloPagina", "Crear cuenta");
    String errorRegistro = (String) request.getAttribute("errorRegistro");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="auth-shell py-5"><div class="container"><div class="row justify-content-center"><div class="col-md-8 col-lg-6"><div class="card border-0 shadow-sm"><div class="card-body p-4 p-lg-5"><div class="text-center mb-4"><img class="auth-logo" src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" alt="Altaltium Real Estate"></div><h1 class="h3 fw-bold text-brand">Crear cuenta</h1><p class="text-secondary">Regístrate como cliente para guardar favoritos y solicitar visitas.</p>
<% if (errorRegistro != null) { %><div class="alert alert-danger" role="alert"><%= errorRegistro %></div><% } %>
<form action="<%= request.getContextPath() %>/auth/registro" method="post" class="row g-3" novalidate><div class="col-md-6"><label class="form-label" for="nombres">Nombres</label><input class="form-control" id="nombres" name="nombres" placeholder="Ej. Joel" required></div><div class="col-md-6"><label class="form-label" for="apellidos">Apellidos</label><input class="form-control" id="apellidos" name="apellidos" placeholder="Ej. Contreras" required></div><div class="col-md-6"><label class="form-label" for="documento">Documento</label><input class="form-control" id="documento" name="documento" placeholder="Ej. 1098765432" required></div><div class="col-md-6"><label class="form-label" for="correo">Correo</label><input class="form-control" id="correo" name="correo" type="email" placeholder="tucorreo@ejemplo.com" required></div><div class="col-12"><label class="form-label" for="password">Contrasena</label><input class="form-control" id="password" name="password" type="password" minlength="8" placeholder="Mínimo 8 caracteres" required></div><div class="col-12 mt-4"><button class="btn btn-accent w-100" type="submit">Crear cuenta</button></div></form></div></div></div></div></div></main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

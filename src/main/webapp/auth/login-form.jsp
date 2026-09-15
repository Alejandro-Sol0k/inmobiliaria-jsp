<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("tituloPagina", "Ingresar");
    String errorLogin = (String) request.getAttribute("errorLogin");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="auth-shell py-5"><div class="container"><div class="row justify-content-center"><div class="col-md-6 col-lg-4"><div class="card border-0 shadow-sm"><div class="card-body p-4 p-lg-5"><div class="text-center mb-4"><img class="auth-logo" src="<%= request.getContextPath() %>/assets/images/altaltium-logo.png" alt="Altaltium Real Estate"></div><h1 class="h3 fw-bold text-brand">Bienvenido</h1><p class="text-secondary">Ingresa para continuar a tu panel.</p>
<% if (errorLogin != null) { %><div class="alert alert-danger" role="alert"><%= errorLogin %></div><% } %>
<% if ("ok".equals(request.getParameter("registro"))) { %><div class="alert alert-success" role="alert">Cuenta creada. Ya puedes iniciar sesion.</div><% } %>
<form action="<%= request.getContextPath() %>/auth/login.jsp" method="post" novalidate><input type="hidden" name="redirect" value="<%= request.getAttribute("redirectAfterLogin") == null ? "" : request.getAttribute("redirectAfterLogin") %>"><div class="mb-3"><label class="form-label" for="correo">Correo</label><input class="form-control" id="correo" name="correo" type="email" placeholder="tucorreo@ejemplo.com" required></div><div class="mb-4"><label class="form-label" for="password">Contrasena</label><input class="form-control" id="password" name="password" type="password" placeholder="Ingresa tu contrasena" required></div><button class="btn btn-accent w-100" type="submit">Ingresar</button></form><p class="small text-secondary mt-4 mb-0">¿Aun no tienes cuenta? <a href="<%= request.getContextPath() %>/auth/registro.jsp">Registrate</a>.</p>
</div></div></div></div></div></main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

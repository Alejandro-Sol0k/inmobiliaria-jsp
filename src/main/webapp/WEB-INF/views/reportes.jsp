<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.edu.uts.inmobiliaria.model.ReportRow,java.util.List" %>
<%
    List<ReportRow> ciudades = (List<ReportRow>) request.getAttribute("ciudades");
    List<ReportRow> operaciones = (List<ReportRow>) request.getAttribute("operaciones");
    List<ReportRow> solicitudesEstado = (List<ReportRow>) request.getAttribute("solicitudesEstado");
    List<ReportRow> solicitudesInmobiliaria = (List<ReportRow>) request.getAttribute("solicitudesInmobiliaria");
    List<ReportRow> citasEstado = (List<ReportRow>) request.getAttribute("citasEstado");
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<%@ include file="/WEB-INF/jspf/navegacion.jspf" %>
<main class="container py-5">
    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4"><div><span class="section-kicker">Administración · SQL</span><h1 class="fw-bold mb-1">Reportes del negocio</h1><p class="text-secondary mb-0">Resumen de propiedades, citas y solicitudes mediante consultas agrupadas.</p></div><a class="btn btn-outline-dark" href="<%= request.getContextPath() %>/app/dashboard.jsp">Volver al panel</a></div>
    <% if (request.getAttribute("errorReportes") != null) { %><div class="alert alert-danger"><%= request.getAttribute("errorReportes") %></div><% } %>
    <div class="row g-4">
        <div class="col-md-6"><div class="card report-card h-100 shadow-sm"><div class="card-body p-4"><h2 class="h5 fw-bold">Propiedades activas por ciudad</h2><div class="gold-line mb-3"></div><% if (ciudades != null) { for (ReportRow row : ciudades) { %><div class="report-row"><span><%= row.getLabel() %></span><strong><%= row.getTotal() %></strong></div><% } } %></div></div></div>
        <div class="col-md-6"><div class="card report-card h-100 shadow-sm"><div class="card-body p-4"><h2 class="h5 fw-bold">Propiedades por operación</h2><div class="gold-line mb-3"></div><% if (operaciones != null) { for (ReportRow row : operaciones) { %><div class="report-row"><span><%= row.getLabel() %></span><strong><%= row.getTotal() %></strong></div><% } } %></div></div></div>
        <div class="col-md-6"><div class="card report-card h-100 shadow-sm"><div class="card-body p-4"><h2 class="h5 fw-bold">Solicitudes por estado</h2><div class="gold-line mb-3"></div><% if (solicitudesEstado != null) { for (ReportRow row : solicitudesEstado) { %><div class="report-row"><span><%= row.getLabel() %></span><strong><%= row.getTotal() %></strong></div><% } } %></div></div></div>
        <div class="col-md-6"><div class="card report-card h-100 shadow-sm"><div class="card-body p-4"><h2 class="h5 fw-bold">Solicitudes por inmobiliaria</h2><div class="gold-line mb-3"></div><% if (solicitudesInmobiliaria != null) { for (ReportRow row : solicitudesInmobiliaria) { %><div class="report-row"><span><%= row.getLabel() %></span><strong><%= row.getTotal() %></strong></div><% } } %></div></div></div>
        <div class="col-md-6"><div class="card report-card h-100 shadow-sm"><div class="card-body p-4"><h2 class="h5 fw-bold">Citas por estado</h2><div class="gold-line mb-3"></div><% if (citasEstado != null) { for (ReportRow row : citasEstado) { %><div class="report-row"><span><%= row.getLabel() %></span><strong><%= row.getTotal() %></strong></div><% } } %></div></div></div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>

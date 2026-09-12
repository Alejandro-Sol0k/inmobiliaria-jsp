# Guía de entrega final

## Alcance

Altaltium Real Estate permite consultar propiedades, autenticarse por roles, gestionar publicaciones, agendar visitas, radicar solicitudes, cargar documentos, guardar favoritos y generar reportes.

## Roles y permisos

| Rol | Permisos principales |
|---|---|
| Visitante | Landing y catálogo público. |
| Cliente | Perfil, favoritos, citas, solicitudes y carga de documentos. |
| Inmobiliaria | Gestión de propiedades, atención de citas/solicitudes, revisión de documentos y reportes. |
| Administrador | Todo lo anterior, más usuarios, roles, catálogos y auditoría. |

La autorización se aplica en el servidor mediante `AuthFilter` y validaciones de los servlets; ocultar un enlace en JSP no es la única medida de seguridad.

## Trazabilidad del parcial

| Requisito | Evidencia en el proyecto |
|---|---|
| Landing y autenticación | `src/main/webapp/index.jsp`, `auth/`, `LoginServlet`, `RegisterServlet` |
| Roles y sesiones | `AuthFilter`, `WEB-INF/jspf/navegacion.jspf`, `AdminServlet` |
| Propiedades, filtros y detalle | `PropertyDao`, `PropertyServlet`, `PropertyManagementServlet`, `detalle-propiedad.jsp` |
| Galería e imágenes | `imagen_propiedad`, `03_consultas_obligatorias.sql`, catálogo y detalle |
| Citas y solicitudes | `OperationServlet`, `OperationDao`, `app/operaciones.jsp` |
| Documentos | `DocumentServlet`, `DocumentDao` |
| Favoritos | `FavoriteServlet`, `FavoriteDao`, `app/favoritos.jsp` |
| Reportes | `ReportServlet`, `ReportDao`, `app/reportes.jsp` |
| Administración y auditoría | `AdminServlet`, `AdminDao`, `AuditDao`, `app/admin.jsp` |
| Consultas SQL | `database/03_consultas_obligatorias.sql` |

## Despliegue con XAMPP y Tomcat

1. Iniciar MySQL desde XAMPP.
2. Ejecutar `database/01_schema.sql`, luego `database/02_seed.sql` y, si se desean validar las consultas, `database/03_consultas_obligatorias.sql` desde phpMyAdmin.
3. Confirmar la conexión en `src/main/webapp/META-INF/context.xml`.
4. Ejecutar `mvn clean package`.
5. Copiar `target/inmobiliaria-jsp.war` a la carpeta `webapps` de Tomcat.
6. Iniciar Tomcat y abrir `/inmobiliaria-jsp/`.

## Git

Los cambios se registran con commits descriptivos. El autor configurado para el repositorio es Joel (`jaconde@uts.edu.co`).

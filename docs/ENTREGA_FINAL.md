# Guía de entrega final

## Alcance

Altaltium Real Estate permite consultar propiedades, autenticarse por roles, gestionar publicaciones, agendar visitas, radicar solicitudes, cargar documentos, guardar favoritos y generar reportes.

## Roles y permisos

| Rol | Permisos principales |
|---|---|
| Visitante no autenticado | Landing, catálogo público y detalle de propiedades; sin paneles internos ni datos de contacto completos. |
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
| Chat privado | `ChatServlet`, `ChatDao`, `app/chat.jsp`, `database/04_chat.sql` |
| Consultas SQL | `database/03_consultas_obligatorias.sql` |

## Despliegue con XAMPP y Tomcat

1. Iniciar MySQL desde XAMPP.
2. Ejecutar `database/01_schema.sql`, luego `database/02_seed.sql`, `database/04_chat.sql` y, si se desean validar las consultas, `database/03_consultas_obligatorias.sql` desde phpMyAdmin.
3. Confirmar la conexión en `src/main/webapp/META-INF/context.xml`.
4. Ejecutar `mvn clean package`.
5. Copiar `target/inmobiliaria-jsp.war` a la carpeta `webapps` de Tomcat.
6. Iniciar Tomcat y abrir `/inmobiliaria-jsp/`.

## Conexión a Clever Cloud

La aplicación prioriza estas variables de entorno cuando están definidas: `INMOBILIARIA_DB_URL`, `INMOBILIARIA_DB_USER` e `INMOBILIARIA_DB_PASSWORD`. La URL debe usar el formato JDBC, por ejemplo `jdbc:mysql://HOST:3306/BASE?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=true`.

En Windows se pueden definir antes de iniciar Tomcat con `setx` o en la configuración del servicio. Después se debe reiniciar Tomcat. El archivo `context-online.xml.example` documenta la configuración sin almacenar la contraseña real.

La conexión remota fue comprobada con el host MySQL proporcionado y respondió correctamente. La migración creó `chat_conversacion` y `chat_mensaje` sin alterar las 16 tablas existentes.

## Sincronización local sin conflictos

La base de Clever Cloud es la fuente única de verdad. En el Tomcat local se configuran las variables remotas y `INMOBILIARIA_SYNC_LOCAL=true`; el listener replica automáticamente todas las tablas en XAMPP cada 10 segundos por defecto. La copia local se reemplaza dentro de una transacción con restricciones foráneas desactivadas temporalmente, por lo que no se hacen escrituras independientes ni se generan IDs duplicados.

## Git

Los cambios se registran con commits descriptivos. El autor configurado para el repositorio es Joel (`jaconde@uts.edu.co`).

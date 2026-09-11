# Plan del proyecto

## Alcance

El PDF del parcial define una aplicacion inmobiliaria con cuatro roles, propiedades, citas, solicitudes, favoritos, reportes, modelo 3FN y trabajo en tres sprints.

## Decisiones de implementacion

- Las vistas y fragmentos reutilizables se organizan en JSP y JSPF.
- Se permiten clases `.java` solo para infraestructura estrictamente necesaria: conexion, seguridad, DAO, servlets y filtros.
- La conexion JDBC se centraliza en `config/Database.java`.
- La validacion de sesion y rol se centraliza en `filter/AuthFilter.java`.
- El esquema SQL conserva relaciones 1:1, 1:N y N:M, restricciones `UNIQUE` y auditoria.
- Los archivos generados por Tomcat no se registran en Git.

## Regla para nuevos archivos Java

Antes de crear una clase Java nueva debe comprobarse si la responsabilidad puede resolverse correctamente con una JSP/JSPF. Si no es infraestructura de servidor, acceso a datos, seguridad o entrada HTTP, se mantiene en JSP/JSPF.

## Criterios de orden

- Una responsabilidad por carpeta.
- Fragmentos comunes en `WEB-INF/jspf`.
- SQL versionado por finalidad y orden de ejecucion.
- Commits pequenos, descriptivos y asociados a un sprint.

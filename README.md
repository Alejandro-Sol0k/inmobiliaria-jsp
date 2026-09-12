# Altaltium Real Estate - JSP/JSPF

Proyecto web academico para administrar una inmobiliaria, construido desde cero con JSP, JSPF, JDBC, MySQL, HTML5, CSS3, JavaScript y Bootstrap.

## Regla tecnica del proyecto

Las vistas se implementan en JSP/JSPF. Solo se usan clases `.java` para la infraestructura que debe ejecutarse en el servidor: conexion, seguridad, DAO, servlets y filtros. Tomcat puede generar archivos Java internamente al compilar las JSP, pero esos artefactos quedan fuera del repositorio mediante `.gitignore`.

## Estructura

```text
database/                         Scripts DDL y datos iniciales
  docs/                             Plan, Scrum, modelo, pruebas y entrega final
src/main/java/co/edu/uts/inmobiliaria/
  config/                           Conexion y configuracion de infraestructura
  controller/                       Servlets de entrada HTTP
  dao/                              Acceso a datos
  filter/                           Proteccion de rutas privadas
  model/                            Objetos de sesion
  security/                         Hash y validacion de contrasenas
src/main/webapp/
  assets/css/                     Estilos globales
  auth/                           Inicio de sesion, registro y cierre
  app/                            Paginas privadas
  WEB-INF/jspf/                   Conexion, seguridad y layout reutilizable
  WEB-INF/web.xml                 Configuracion de la aplicacion
  META-INF/context.xml            Recurso JDBC para Tomcat
  index.jsp                       Landing page publica
```

## Puesta en marcha

1. Ejecutar `database/01_schema.sql` y `database/02_seed.sql` en MySQL.
2. Configurar el recurso `jdbc/inmobiliaria` en `META-INF/context.xml` o en la configuracion del servidor Tomcat. En XAMPP se deja `password=""` si el usuario `root` no tiene contraseña.
3. Copiar el conector JDBC de MySQL en `WEB-INF/lib` sin versionarlo si el entorno lo administra externamente.
4. Desplegar `src/main/webapp` como aplicacion web en Tomcat.
5. Abrir `index.jsp`.

## Estado actual

La primera iteracion deja lista la estructura base, la landing page, el registro, el inicio de sesion con contrasena almacenada mediante PBKDF2, las sesiones por rol, el filtro de rutas privadas y el esquema relacional inicial. El catalogo publico consulta propiedades e imagenes desde MySQL, permite filtros basicos y enlaza al detalle con galería. El Sprint 2 incluye el CRUD basico de propiedades, imagenes por URL o carga local, características, perfil 1:1 y una interfaz visual alineada con la marca Altaltium. El Sprint 3 incorpora citas, solicitudes, carga de documentos, favoritos, chat privado cliente-inmobiliaria, reportes SQL por ciudad, estado, operación e inmobiliaria y el panel administrativo.

El seed contiene diez propiedades de demostración, imágenes remotas y asociaciones N:M con características. Las cinco consultas académicas obligatorias están documentadas en `database/03_consultas_obligatorias.sql`. El panel administrativo también conserva un historial reciente de auditoría para acciones de autenticación, usuarios, propiedades y operaciones.

La documentación de entrega está en `docs/ENTREGA_FINAL.md`, `docs/SCRUM.md`, `docs/MODELO-RELACIONAL.md` y `docs/PRUEBAS.md`.

La conexión puede apuntar a XAMPP mediante JNDI o a una base MySQL en línea mediante `INMOBILIARIA_DB_URL`, `INMOBILIARIA_DB_USER` e `INMOBILIARIA_DB_PASSWORD`. El chat requiere ejecutar `database/04_chat.sql` en la base seleccionada.

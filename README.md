# Altaltium Real Estate - JSP/JSPF

Proyecto web academico para administrar una inmobiliaria, construido desde cero con JSP, JSPF, JDBC, MySQL, HTML5, CSS3, JavaScript y Bootstrap.

## Regla tecnica del proyecto

Las vistas se implementan en JSP/JSPF. Solo se usan clases `.java` para la infraestructura que debe ejecutarse en el servidor: conexion, seguridad, DAO, servlets y filtros. Tomcat puede generar archivos Java internamente al compilar las JSP, pero esos artefactos quedan fuera del repositorio mediante `.gitignore`.

## Estructura

```text
database/                         Scripts DDL y datos iniciales
docs/                             Plan del proyecto y sprints
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

La primera iteracion deja lista la estructura base, la landing page, el registro, el inicio de sesion con contrasena almacenada mediante PBKDF2, las sesiones por rol, el filtro de rutas privadas y el esquema relacional inicial. El catalogo publico ya consulta propiedades e imagenes desde MySQL y permite filtros basicos. El Sprint 2 incluye el CRUD basico de propiedades y una interfaz visual alineada con la marca Altaltium.

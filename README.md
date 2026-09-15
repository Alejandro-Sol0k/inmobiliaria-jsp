# Altaltium Real Estate - JSP/JSPF

Proyecto web academico para administrar una inmobiliaria, construido desde cero con JSP, JSPF, JDBC, MySQL, HTML5, CSS3 y Bootstrap.


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
  inicio.jsp                      Ruta publica de la landing atendida por HomeServlet
```

## Puesta en marcha

1. Ejecutar `database/01_schema.sql` y `database/02_seed.sql` en MySQL.
2. Configurar el recurso `jdbc/inmobiliaria` en `META-INF/context.xml` o en la configuracion del servidor Tomcat. En XAMPP se deja `password=""` si el usuario `root` no tiene contraseña.
3. Copiar el conector JDBC de MySQL en `WEB-INF/lib` sin versionarlo si el entorno lo administra externamente.
4. Desplegar `src/main/webapp` como aplicacion web en Tomcat.

## Rutas públicas atendidas por servlets

- `inicio.jsp`: landing y propiedades destacadas.
- `propiedades.jsp`: catálogo y filtros.
- `propiedad.jsp?id=3`: detalle de una propiedad.
- `auth/login.jsp`: inicio de sesión.
- `auth/login.jsp?redirect=%2Fpropiedad.jsp%3Fid%3D3`: inicio de sesión y retorno al detalle solicitado.

# Sprints

## Sprint 1 - Cimientos y acceso

- [x] Crear repositorio Git y estructura JSP/JSPF.
- [x] Definir esquema relacional inicial.
- [x] Crear landing page responsiva.
- [x] Crear registro e inicio de sesion.
- [x] Crear control de sesion y rol en JSPF.
- [x] Ejecutar y validar el esquema contra MySQL local.
- [x] Cargar propiedades e imagenes de prueba para el catalogo.

## Sprint 2 - Catalogo y perfiles

- [x] CRUD básico de propiedades con publicación y baja lógica.
- [x] Imagenes, galería y características.
- [x] Buscador con filtros por ciudad, tipo, operacion y precio.
- [x] Detalle público de propiedad con galería y características.
- [x] Perfil 1:1 del usuario.
- [x] Dashboards diferenciados.
- [x] Panel administrativo de usuarios, roles y catálogos.
- [x] Auditoría de acciones y consulta de historial para el administrador.

## Sprint 3 - Operacion y cierre

- [x] Citas y solicitudes.
- [x] Documentos y favoritos.
- [x] Chat privado entre cliente e inmobiliaria con cierre de conversación.
- [x] Reportes SQL con joins, agrupaciones, solicitudes por inmobiliaria y agregaciones.
- [x] Documentacion final, matriz de pruebas y guia de despliegue.
- [x] Ejecucion y evidencia de pruebas funcionales en XAMPP/Tomcat.

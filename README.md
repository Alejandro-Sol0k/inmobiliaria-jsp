# Altaltium Real Estate - JSP/JSPF

Proyecto web academico para administrar una inmobiliaria, construido desde cero con JSP, JSPF, JDBC, MySQL, HTML5, CSS3 y Bootstrap.

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
- [ ] Ejecucion y evidencia de pruebas funcionales en XAMPP/Tomcat.

La conexión puede apuntar a XAMPP mediante JNDI o a una base MySQL en línea mediante `INMOBILIARIA_DB_URL`, `INMOBILIARIA_DB_USER` e `INMOBILIARIA_DB_PASSWORD`. Para mantener una réplica local de XAMPP, configura además `INMOBILIARIA_SYNC_LOCAL=true` en el Tomcat local. Clever Cloud permanece como fuente única de verdad; el listener actualiza automáticamente la réplica local cada 10 segundos por defecto. El intervalo puede ajustarse con `INMOBILIARIA_SYNC_INTERVAL_SECONDS` (mínimo 10 segundos). El chat requiere ejecutar `database/04_chat.sql` en la base seleccionada.

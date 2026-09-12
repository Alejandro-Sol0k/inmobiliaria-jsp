# Evidencia Scrum

## Backlog priorizado

| Prioridad | Historia de usuario | Criterio de aceptación |
|---|---|---|
| Alta | Como visitante quiero consultar propiedades | El catálogo carga propiedades activas y permite filtrar. |
| Alta | Como cliente quiero registrarme e iniciar sesión | La contraseña se almacena con PBKDF2 y se crea el perfil. |
| Alta | Como inmobiliaria quiero publicar y editar propiedades | El formulario acepta URL o archivo, características y baja lógica. |
| Alta | Como cliente quiero solicitar una visita | Se valida propiedad activa, fecha futura y disponibilidad horaria. |
| Alta | Como inmobiliaria quiero atender operaciones | Puede cambiar estados respetando las transiciones permitidas. |
| Media | Como cliente quiero cargar documentos y guardar favoritos | Solo puede gestionar sus solicitudes y favoritos. |
| Media | Como administrador quiero gestionar usuarios y catálogos | Puede crear usuarios, asignar roles y activar/inactivar cuentas. |
| Media | Como administrador quiero consultar auditoría | El panel muestra las últimas acciones registradas. |

## Sprint 1 - Cimientos y acceso

- Planificación: estructura JSP/JSPF, esquema MySQL, autenticación y sesiones.
- Revisión: landing, registro, login, PBKDF2, filtro de autenticación y conexión JDBC.
- Retrospectiva: centralizar la conexión y separar vistas de la infraestructura Java.

## Sprint 2 - Catálogo y perfiles

- Planificación: CRUD, filtros, detalle, galería, perfil y administración.
- Revisión: publicaciones activas, baja lógica, imagen por URL/archivo, roles y catálogos.
- Retrospectiva: mejorar la presentación de precios, imágenes, formularios y permisos.

## Sprint 3 - Operación y cierre

- Planificación: citas, solicitudes, documentos, favoritos, reportes, auditoría y entrega.
- Revisión: flujo de operaciones, transiciones de estado, cinco consultas SQL y panel administrativo.
- Retrospectiva: dejar pruebas manuales y evidencias visuales como actividad final del equipo.

## Evidencia Git

La historia de commits del repositorio funciona como evidencia de avance incremental. Para la entrega se recomienda adjuntar capturas de la vista de commits, del catálogo, de los paneles por rol y de las pruebas ejecutadas en XAMPP/Tomcat.

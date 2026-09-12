# Matriz de pruebas y despliegue

## Verificación automatizada

| Prueba | Resultado |
|---|---|
| `mvn clean package` | Aprobada: WAR generado correctamente. |
| `git diff --check` | Aprobada: sin errores de espacios/formato. |
| Compilación Java | Aprobada con Java 11; 34 clases compiladas. |
| Pruebas unitarias Maven | Pendiente: todavía no hay fuentes en `src/test`. |

## Pruebas funcionales en XAMPP/Tomcat

| ID | Caso | Resultado esperado |
|---|---|---|
| F01 | Abrir `/inmobiliaria-jsp/` | Se muestra la landing y el catálogo público. |
| F02 | Registrar cliente con correo/documento nuevos | Se crea usuario, perfil y rol CLIENTE. |
| F03 | Iniciar sesión | Redirección al catálogo de propiedades. |
| F04 | Filtrar por ciudad, tipo, operación y precio | Solo aparecen propiedades activas coincidentes. |
| F05 | Clic en imagen del catálogo | Se abre el detalle de la propiedad. |
| F06 | Solicitar visita desde detalle | Operaciones abre con propiedad e imagen seleccionadas. |
| F07 | Cambiar estado de cita | Solo se permiten transiciones válidas. |
| F08 | Crear propiedad como Inmobiliaria | Se guarda imagen por URL o archivo y características. |
| F09 | Crear usuario como Administrador | Se crea perfil, contraseña PBKDF2 y rol seleccionado. |
| F10 | Consultar auditoría como Administrador | Se muestran las acciones recientes. |
| F11 | Intentar `/app/admin` como Inmobiliaria | El servidor responde 403. |
| F12 | Cargar documento de solicitud | El cliente lo carga y el gestor puede cambiar su estado. |
| F13 | Crear chat como cliente | Se asigna a un inmobiliario activo y solo los participantes ven los mensajes. |
| F14 | Responder y cerrar chat | Cliente/inmobiliario pueden responder mientras está abierto; solo el gestor puede cerrarlo. |
| F15 | Conexión online | JDBC conecta al host remoto y la migración deja dos tablas nuevas de chat. |
| F16 | Sincronización de réplica | Con `INMOBILIARIA_SYNC_LOCAL=true`, el listener actualiza automáticamente XAMPP desde Clever Cloud sin doble escritura. |

Estas pruebas deben ejecutarse en el equipo con MySQL, XAMPP y Tomcat activos; se recomienda adjuntar una captura por flujo a la entrega.

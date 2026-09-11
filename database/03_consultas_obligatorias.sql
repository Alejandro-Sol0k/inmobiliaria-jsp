USE inmobiliaria_uts;

-- 1. Propiedades con ciudad, tipo e inmobiliaria (INNER JOIN de 4 tablas).
SELECT p.titulo, c.nombre AS ciudad, tp.nombre AS tipo, i.nombre AS inmobiliaria
FROM propiedad p
INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad
INNER JOIN tipo_propiedad tp ON tp.id_tipo = p.id_tipo
INNER JOIN inmobiliaria i ON i.id_inmobiliaria = p.id_inmobiliaria
WHERE p.disponible = TRUE;

-- 2. Citas con propiedad, cliente y perfil (INNER JOIN de 4 tablas).
SELECT ci.fecha_hora, p.titulo, u.correo, CONCAT(pf.nombres, ' ', pf.apellidos) AS cliente
FROM cita ci
INNER JOIN propiedad p ON p.id_propiedad = ci.id_propiedad
INNER JOIN usuario u ON u.id_usuario = ci.id_cliente
INNER JOIN perfil pf ON pf.id_usuario = u.id_usuario;

-- 3. Relación N:M: características asociadas a cada propiedad.
SELECT p.titulo, c.nombre AS caracteristica
FROM propiedad p
INNER JOIN propiedad_caracteristica pc ON pc.id_propiedad = p.id_propiedad
INNER JOIN caracteristica c ON c.id_caracteristica = pc.id_caracteristica;

-- 4. LEFT JOIN: propiedades que todavía no tienen citas.
SELECT p.titulo, p.matricula_inmobiliaria
FROM propiedad p
LEFT JOIN cita ci ON ci.id_propiedad = p.id_propiedad
WHERE p.disponible = TRUE AND ci.id_cita IS NULL;

-- 5. Agregación con GROUP BY y HAVING: ciudades con al menos una propiedad activa.
SELECT c.nombre AS ciudad, COUNT(*) AS propiedades_activas
FROM propiedad p
INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad
WHERE p.disponible = TRUE
GROUP BY c.id_ciudad, c.nombre
HAVING COUNT(*) >= 1
ORDER BY propiedades_activas DESC;

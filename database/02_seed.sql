USE inmobiliaria_uts;

INSERT INTO rol (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Acceso total al sistema'),
('INMOBILIARIA', 'Gestiona propiedades y solicitudes'),
('CLIENTE', 'Consulta propiedades y tramites'),
('VISITANTE', 'Consulta el catalogo publico')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

INSERT INTO ciudad (nombre) VALUES
('Bucaramanga'), ('Floridablanca'), ('Girón'), ('Piedecuesta')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO tipo_propiedad (nombre) VALUES
('Casa'), ('Apartamento'), ('Local'), ('Oficina'), ('Terreno')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO caracteristica (nombre) VALUES
('Piscina'), ('Parqueadero'), ('Ascensor'), ('Gimnasio'), ('Vigilancia')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO inmobiliaria (nombre, correo, telefono, direccion) VALUES
('Inmobiliaria UTS', 'contacto@inmobiliaria-uts.test', '6076000000', 'Carrera 27 # 9-50')
ON DUPLICATE KEY UPDATE correo = VALUES(correo);

INSERT INTO propiedad (id_inmobiliaria, id_ciudad, id_tipo, matricula_inmobiliaria, titulo, descripcion, direccion, precio, operacion, habitaciones, banos, area_m2)
VALUES
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Inmobiliaria UTS'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Bucaramanga'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Casa'), 'UTS-CASA-001', 'Casa Brisas del Rio', 'Casa amplia con espacios familiares.', 'Calle 45 # 12-30', 480000000, 'VENTA', 3, 2, 145.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Inmobiliaria UTS'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Floridablanca'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Apartamento'), 'UTS-APT-001', 'Apartamento La Riviera', 'Apartamento iluminado con parqueadero.', 'Carrera 22 # 35-18', 1850000, 'ARRIENDO', 2, 2, 78.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Inmobiliaria UTS'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Bucaramanga'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Oficina'), 'UTS-OFI-001', 'Oficina Cabecera', 'Oficina moderna para equipos de trabajo.', 'Carrera 33 # 48-20', 3200000, 'ARRIENDO', 0, 1, 85.00)
ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), precio = VALUES(precio), disponible = TRUE;

INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=900&q=80', 'Casa moderna', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-CASA-001'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=900&q=80', 'Apartamento iluminado', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-APT-001'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=900&q=80', 'Oficina moderna', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-OFI-001'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);

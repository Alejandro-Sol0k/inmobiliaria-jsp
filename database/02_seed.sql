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

UPDATE inmobiliaria
SET nombre = 'Altaltium Real Estate', correo = 'contacto@altaltium.test'
WHERE nombre = 'Inmobiliaria UTS';

INSERT INTO inmobiliaria (nombre, correo, telefono, direccion) VALUES
('Altaltium Real Estate', 'contacto@altaltium.test', '6076000000', 'Carrera 27 # 9-50')
ON DUPLICATE KEY UPDATE correo = VALUES(correo);

INSERT INTO propiedad (id_inmobiliaria, id_ciudad, id_tipo, matricula_inmobiliaria, titulo, descripcion, direccion, precio, operacion, habitaciones, banos, area_m2)
VALUES
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Bucaramanga'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Casa'), 'UTS-CASA-001', 'Casa Brisas del Rio', 'Casa amplia con espacios familiares.', 'Calle 45 # 12-30', 480000000, 'VENTA', 3, 2, 145.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Floridablanca'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Apartamento'), 'UTS-APT-001', 'Apartamento La Riviera', 'Apartamento iluminado con parqueadero.', 'Carrera 22 # 35-18', 1850000, 'ARRIENDO', 2, 2, 78.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Bucaramanga'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Oficina'), 'UTS-OFI-001', 'Oficina Cabecera', 'Oficina moderna para equipos de trabajo.', 'Carrera 33 # 48-20', 3200000, 'ARRIENDO', 0, 1, 85.00)
ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), precio = VALUES(precio), disponible = TRUE;

INSERT INTO propiedad (id_inmobiliaria, id_ciudad, id_tipo, matricula_inmobiliaria, titulo, descripcion, direccion, precio, operacion, habitaciones, banos, area_m2)
VALUES
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Girón'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Casa'), 'UTS-CASA-002', 'Casa Campestre El Limonal', 'Casa campestre con zona verde y espacios amplios.', 'Vereda El Limonal # 8-14', 690000000, 'VENTA', 4, 3, 230.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Piedecuesta'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Apartamento'), 'UTS-APT-002', 'Apartamento Reserva Real', 'Apartamento familiar con excelente iluminación natural.', 'Calle 12 # 7-45', 265000000, 'VENTA', 3, 2, 92.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Bucaramanga'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Local'), 'UTS-LOC-001', 'Local Comercial San Francisco', 'Local sobre vía principal para comercio o servicios.', 'Carrera 22 # 15-60', 4500000, 'ARRIENDO', 0, 1, 64.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Floridablanca'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Casa'), 'UTS-CASA-003', 'Casa Bosques de Cañaveral', 'Casa remodelada cerca de centros comerciales y colegios.', 'Calle 31 # 26-18', 520000000, 'VENTA', 4, 3, 180.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Bucaramanga'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Apartamento'), 'UTS-APT-003', 'Apartamento Altos de Mejoras', 'Apartamento contemporáneo con balcón y parqueadero.', 'Carrera 35 # 42-09', 2850000, 'ARRIENDO', 2, 2, 81.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Girón'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Terreno'), 'UTS-TER-001', 'Lote Industrial La Isla', 'Lote plano con acceso para proyectos industriales.', 'Autopista Girón Km 4', 980000000, 'VENTA', 0, 0, 1250.00),
((SELECT id_inmobiliaria FROM inmobiliaria WHERE nombre = 'Altaltium Real Estate'), (SELECT id_ciudad FROM ciudad WHERE nombre = 'Piedecuesta'), (SELECT id_tipo FROM tipo_propiedad WHERE nombre = 'Oficina'), 'UTS-OFI-002', 'Oficina Centro Empresarial', 'Oficina lista para consultorio o equipo profesional.', 'Carrera 6 # 10-24', 2200000, 'ARRIENDO', 0, 1, 52.00)
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

INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=900&q=80', 'Casa campestre', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-CASA-002'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=900&q=80', 'Apartamento familiar', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-APT-002'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1556761175-b413da4baf72?auto=format&fit=crop&w=900&q=80', 'Local comercial', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-LOC-001'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=900&q=80', 'Casa remodelada', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-CASA-003'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=900&q=80', 'Apartamento contemporáneo', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-APT-003'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=900&q=80', 'Lote industrial', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-TER-001'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);
INSERT INTO imagen_propiedad (id_propiedad, url, texto_alternativo, es_principal)
SELECT id_propiedad, 'https://images.unsplash.com/photo-1497366811353-6870744d04b2?auto=format&fit=crop&w=900&q=80', 'Oficina empresarial', TRUE FROM propiedad WHERE matricula_inmobiliaria = 'UTS-OFI-002'
AND NOT EXISTS (SELECT 1 FROM imagen_propiedad WHERE id_propiedad = propiedad.id_propiedad);

INSERT IGNORE INTO propiedad_caracteristica (id_propiedad, id_caracteristica)
SELECT p.id_propiedad, c.id_caracteristica FROM propiedad p CROSS JOIN caracteristica c
WHERE p.matricula_inmobiliaria IN ('UTS-CASA-001', 'UTS-CASA-002', 'UTS-CASA-003')
AND c.nombre IN ('Parqueadero', 'Vigilancia');
INSERT IGNORE INTO propiedad_caracteristica (id_propiedad, id_caracteristica)
SELECT p.id_propiedad, c.id_caracteristica FROM propiedad p CROSS JOIN caracteristica c
WHERE p.matricula_inmobiliaria IN ('UTS-APT-001', 'UTS-APT-002', 'UTS-APT-003')
AND c.nombre IN ('Ascensor', 'Parqueadero');
INSERT IGNORE INTO propiedad_caracteristica (id_propiedad, id_caracteristica)
SELECT p.id_propiedad, c.id_caracteristica FROM propiedad p CROSS JOIN caracteristica c
WHERE p.matricula_inmobiliaria IN ('UTS-OFI-001', 'UTS-OFI-002')
AND c.nombre IN ('Ascensor', 'Vigilancia');

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

CREATE DATABASE IF NOT EXISTS inmobiliaria_uts
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE inmobiliaria_uts;

CREATE TABLE IF NOT EXISTS rol (
    id_rol INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    correo VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS usuario_rol (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    asignado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS perfil (
    id_perfil INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL UNIQUE,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    documento VARCHAR(30) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    direccion VARCHAR(180),
    foto_url VARCHAR(255),
    CONSTRAINT fk_perfil_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ciudad (
    id_ciudad INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tipo_propiedad (
    id_tipo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS inmobiliaria (
    id_inmobiliaria INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL UNIQUE,
    correo VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    direccion VARCHAR(180)
);

CREATE TABLE IF NOT EXISTS propiedad (
    id_propiedad INT PRIMARY KEY AUTO_INCREMENT,
    id_inmobiliaria INT NOT NULL,
    id_ciudad INT NOT NULL,
    id_tipo INT NOT NULL,
    matricula_inmobiliaria VARCHAR(60) NOT NULL UNIQUE,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    direccion VARCHAR(180) NOT NULL,
    precio DECIMAL(14,2) NOT NULL,
    operacion ENUM('VENTA', 'ARRIENDO') NOT NULL,
    habitaciones TINYINT UNSIGNED NOT NULL DEFAULT 0,
    banos TINYINT UNSIGNED NOT NULL DEFAULT 0,
    area_m2 DECIMAL(10,2) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_propiedad_inmobiliaria FOREIGN KEY (id_inmobiliaria) REFERENCES inmobiliaria(id_inmobiliaria)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_propiedad_ciudad FOREIGN KEY (id_ciudad) REFERENCES ciudad(id_ciudad)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_propiedad_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_propiedad(id_tipo)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS imagen_propiedad (
    id_imagen INT PRIMARY KEY AUTO_INCREMENT,
    id_propiedad INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    texto_alternativo VARCHAR(150),
    es_principal BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_imagen_propiedad FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS caracteristica (
    id_caracteristica INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS propiedad_caracteristica (
    id_propiedad INT NOT NULL,
    id_caracteristica INT NOT NULL,
    cantidad VARCHAR(30),
    PRIMARY KEY (id_propiedad, id_caracteristica),
    CONSTRAINT fk_pc_propiedad FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_pc_caracteristica FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS cita (
    id_cita INT PRIMARY KEY AUTO_INCREMENT,
    id_propiedad INT NOT NULL,
    id_cliente INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'ATENDIDA') NOT NULL DEFAULT 'PENDIENTE',
    observaciones VARCHAR(255),
    UNIQUE KEY uq_cita_propiedad_hora (id_propiedad, fecha_hora),
    CONSTRAINT fk_cita_propiedad FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_cita_cliente FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS solicitud (
    id_solicitud INT PRIMARY KEY AUTO_INCREMENT,
    id_propiedad INT NOT NULL,
    id_cliente INT NOT NULL,
    tipo_operacion ENUM('COMPRA', 'ARRIENDO') NOT NULL,
    estado ENUM('RADICADA', 'EN_REVISION', 'APROBADA', 'RECHAZADA') NOT NULL DEFAULT 'RADICADA',
    creada_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_solicitud_propiedad FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_solicitud_cliente FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS documento_solicitud (
    id_documento INT PRIMARY KEY AUTO_INCREMENT,
    id_solicitud INT NOT NULL,
    nombre_archivo VARCHAR(180) NOT NULL,
    url_archivo VARCHAR(255) NOT NULL,
    estado ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_documento_solicitud FOREIGN KEY (id_solicitud) REFERENCES solicitud(id_solicitud)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS favorito (
    id_cliente INT NOT NULL,
    id_propiedad INT NOT NULL,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_cliente, id_propiedad),
    CONSTRAINT fk_favorito_cliente FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_favorito_propiedad FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS auditoria (
    id_auditoria BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NULL,
    accion VARCHAR(80) NOT NULL,
    entidad VARCHAR(80) NOT NULL,
    id_entidad VARCHAR(40),
    detalle VARCHAR(255),
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE SET NULL ON UPDATE CASCADE
);

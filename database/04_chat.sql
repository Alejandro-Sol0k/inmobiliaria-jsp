-- Ejecutar con la base de datos del proyecto seleccionada.
-- No usa USE para que también funcione con el nombre de base asignado por Clever Cloud.

CREATE TABLE IF NOT EXISTS chat_conversacion (
    id_chat INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_inmobiliaria INT NOT NULL,
    id_propiedad INT NULL,
    asunto VARCHAR(150) NOT NULL,
    estado ENUM('ABIERTA', 'CERRADA') NOT NULL DEFAULT 'ABIERTA',
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cerrado_en TIMESTAMP NULL,
    CONSTRAINT fk_chat_cliente FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chat_inmobiliaria FOREIGN KEY (id_inmobiliaria) REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chat_propiedad FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_mensaje (
    id_mensaje BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_chat INT NOT NULL,
    id_remitente INT NOT NULL,
    mensaje TEXT NOT NULL,
    enviado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mensaje_chat FOREIGN KEY (id_chat) REFERENCES chat_conversacion(id_chat)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mensaje_remitente FOREIGN KEY (id_remitente) REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

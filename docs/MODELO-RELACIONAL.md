# Modelo relacional

## Diagrama lógico

```mermaid
erDiagram
    ROL ||--o{ USUARIO_ROL : asigna
    USUARIO ||--o{ USUARIO_ROL : tiene
    USUARIO ||--|| PERFIL : posee
    INMOBILIARIA ||--o{ PROPIEDAD : publica
    CIUDAD ||--o{ PROPIEDAD : ubica
    TIPO_PROPIEDAD ||--o{ PROPIEDAD : clasifica
    PROPIEDAD ||--o{ IMAGEN_PROPIEDAD : contiene
    PROPIEDAD ||--o{ PROPIEDAD_CARACTERISTICA : relaciona
    CARACTERISTICA ||--o{ PROPIEDAD_CARACTERISTICA : describe
    PROPIEDAD ||--o{ CITA : recibe
    USUARIO ||--o{ CITA : solicita
    PROPIEDAD ||--o{ SOLICITUD : recibe
    USUARIO ||--o{ SOLICITUD : radica
    SOLICITUD ||--o{ DOCUMENTO_SOLICITUD : soporta
    USUARIO ||--o{ FAVORITO : guarda
    PROPIEDAD ||--o{ FAVORITO : es_guardada
    USUARIO ||--o{ AUDITORIA : ejecuta
```

## Tablas y normalización

- `usuario` y `perfil` implementan la relación 1:1.
- `inmobiliaria`, `ciudad` y `tipo_propiedad` se relacionan 1:N con `propiedad`.
- `propiedad` e `imagen_propiedad` representan una relación 1:N para la galería.
- `propiedad` y `caracteristica` se resuelven con `propiedad_caracteristica` como relación N:M.
- `usuario` y `propiedad` se relacionan mediante `cita`, `solicitud` y `favorito` según el caso.
- Las claves únicas principales son correo, documento, matrícula inmobiliaria, nombres de catálogos y correo/nombre de inmobiliaria.

El DDL completo se encuentra en `database/01_schema.sql` y los datos de demostración en `database/02_seed.sql`.

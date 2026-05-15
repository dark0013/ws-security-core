-- Script para insertar roles por defecto
-- Ejecutar después de crear las tablas

INSERT IGNORE INTO roles (rol, estado, usuario_creacion, fecha_creacion, usuario_actualizacion, fecha_actualizacion)
VALUES
    ('ADMIN', true, 'system', NOW(), 'system', NOW()),
    ('USER', true, 'system', NOW(), 'system', NOW()),
    ('MODERATOR', true, 'system', NOW(), 'system', NOW());

-- Verificar que se insertaron correctamente
SELECT * FROM roles;

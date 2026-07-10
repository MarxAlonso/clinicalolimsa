-- ============================================================
-- TEST USERS - Clinica Lolimsa
-- Password for all users: 123456
-- BCrypt hash: $2b$10$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK
-- ============================================================

-- Gerente (back-office, full access)
INSERT IGNORE INTO users (first_name, last_name, email, phone, address, password, role, created_at)
VALUES ('Admin', 'Gerente', 'admin@clinicalolimsa.com', '999999001', 'Av. Principal 123 - Lima',
        '$2b$10$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK', 'gerente', NOW());

-- Medico 1: Pediatria
INSERT IGNORE INTO medicos (nombres, apellidos, dni, fecha_nacimiento, correo, contrasena, telefono, direccion,
                            sueldo, especialidad, numero_colegiatura, universidad, fecha_graduacion,
                            tiempo_experiencia, turno, genero, fecha_contratacion)
VALUES ('Carlos', 'Gutierrez', '12345678', '1980-05-15', 'carlos.gutierrez@clinicalolimsa.com',
        '$2b$10$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK',
        '999999002', 'Av. Salud 456 - Lima',
        8000.00, 'Pediatria', 'CMP-12345', 'Universidad Nacional Mayor de San Marcos',
        '2005-03-20', 18, 'Mañana', 'Masculino', '2010-01-15');

-- Medico 2: Cardiologia
INSERT IGNORE INTO medicos (nombres, apellidos, dni, fecha_nacimiento, correo, contrasena, telefono, direccion,
                            sueldo, especialidad, numero_colegiatura, universidad, fecha_graduacion,
                            tiempo_experiencia, turno, genero, fecha_contratacion)
VALUES ('Maria', 'Lopez', '87654321', '1985-08-22', 'maria.lopez@clinicalolimsa.com',
        '$2b$10$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK',
        '999999003', 'Av. Corazón 789 - Lima',
        9500.00, 'Cardiologia', 'CMP-67890', 'Universidad Peruana Cayetano Heredia',
        '2008-07-10', 15, 'Tarde', 'Femenino', '2012-03-01');

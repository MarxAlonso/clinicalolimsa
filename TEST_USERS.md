# Usuarios de Prueba

Contraseña para **todos** los usuarios: `123456`

---

## Gerente (acceso full — back-office)

| Campo    | Valor                                |
| -------- | ------------------------------------ |
| Email    | `admin@clinicalolimsa.com`          |
| Password | `123456`                             |
| Rol      | `gerente`                            |

---

## Médicos (acceso al módulo médico)

| Campo    | Médico 1 (Pediatría)                 | Médico 2 (Cardiología)               |
| -------- | ------------------------------------ | ------------------------------------ |
| Email    | `carlos.gutierrez@clinicalolimsa.com` | `maria.lopez@clinicalolimsa.com`    |
| Password | `123456`                             | `123456`                             |
| Turno    | Mañana                               | Tarde                                |

**Importante:** Solo médicos con especialidad `Pediatria` o `Cardiologia` pueden iniciar sesión.

---

## Paciente (acceso público — registro web)

| Campo    | Valor                               |
| -------- | ----------------------------------- |
| Email    | cualquier correo                    |
| Password | cualquiera (definido al registrarse) |

Crear un paciente desde: `http://localhost:8083/register`

---

## Cómo insertar los datos

```bash
# Opción 1 — Ejecutar seed.sql con docker
docker compose exec -T mysql mysql -u root bd_lolimsa < seed.sql
```

```bash
# Opción 2 — Insertar manualmente con docker
docker compose exec -T mysql mysql -u root bd_lolimsa -e "INSERT IGNORE INTO users (first_name, last_name, email, phone, address, password, role, created_at) VALUES ('Admin', 'Gerente', 'admin@clinicalolimsa.com', '999999001', 'Av. Principal 123 - Lima', '\$2b\$10\$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK', 'gerente', NOW());"

docker compose exec -T mysql mysql -u root bd_lolimsa -e "INSERT IGNORE INTO medicos (nombres, apellidos, dni, fecha_nacimiento, correo, contrasena, telefono, direccion, sueldo, especialidad, numero_colegiatura, universidad, fecha_graduacion, tiempo_experiencia, turno, genero, fecha_contratacion) VALUES ('Carlos', 'Gutierrez', '12345678', '1980-05-15', 'carlos.gutierrez@clinicalolimsa.com', '\$2b\$10\$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK', '999999002', 'Av. Salud 456 - Lima', 8000.00, 'Pediatria', 'CMP-12345', 'Universidad Nacional Mayor de San Marcos', '2005-03-20', 18, 'Mañana', 'Masculino', '2010-01-15');"

docker compose exec -T mysql mysql -u root bd_lolimsa -e "INSERT IGNORE INTO medicos (nombres, apellidos, dni, fecha_nacimiento, correo, contrasena, telefono, direccion, sueldo, especialidad, numero_colegiatura, universidad, fecha_graduacion, tiempo_experiencia, turno, genero, fecha_contratacion) VALUES ('Maria', 'Lopez', '87654321', '1985-08-22', 'maria.lopez@clinicalolimsa.com', '\$2b\$10\$X99Kv/rPScHPylnqLw5.d.hJ7t.T0yYDsO5T.qHgG9SVEUZIDOpZK', '999999003', 'Av. Corazón 789 - Lima', 9500.00, 'Cardiologia', 'CMP-67890', 'Universidad Peruana Cayetano Heredia', '2008-07-10', 15, 'Tarde', 'Femenino', '2012-03-01');"
```

> **Nota:** Si la app está corriendo y los datos no se ven reflejados, detener el contenedor app y volver a iniciarlo: `docker compose restart app`.

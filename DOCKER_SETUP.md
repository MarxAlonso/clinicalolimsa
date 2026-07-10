# Ejecución del Proyecto con Docker (Windows)

## Requisitos

1. **Docker Desktop** instalado y funcionando en Windows
   - Descargar desde: https://www.docker.com/products/docker-desktop/
   - Asegúrate de que el motor de Docker (WSL 2 / Hyper-V) esté corriendo
2. **Git Bash** o **PowerShell** para ejecutar comandos
3. **Puertos libres:** `3309` (MySQL) y `8083` (aplicación)

## Pasos

### 1. Clonar / ubicarse en el proyecto

```bash
cd "C:\developer-marx\Proyectos UTP\clinicalolimsa"
```

### 2. Construir imágenes y levantar servicios

```bash
docker compose up --build -d
```

Esto ejecuta:
- Construcción de la imagen Spring Boot (Maven compila el `.jar`, JRE lo ejecuta)
- Descarga de `mysql:8.0` (si no está en caché)
- Creación de la base de datos `bd_lolimsa` automáticamente
- Montaje del directorio `uploads/` local dentro del contenedor

La primera vez puede tardar **5–10 minutos** (descarga de dependencias Maven + imágenes Docker).

### 3. Verificar que todo está corriendo

```bash
docker compose ps
```

Deberías ver dos servicios con estado `Up`:

| Servicio | Puerto (host) | Container name |
|---|---|---|
| `mysql` | `3309` | `clinicalolimsa-mysql` |
| `app` | `8083` | `clinicalolimsa-app` |

### 4. Ver logs (opcional)

```bash
docker compose logs -f app
docker compose logs -f mysql
```

Presiona `Ctrl + C` para salir de los logs.

### 5. Abrir la aplicación

```
http://localhost:8083
```

## Comandos útiles

| Acción | Comando |
|---|---|
| Detener servicios | `docker compose down` |
| Detener y borrar volúmenes (pierde datos DB) | `docker compose down -v` |
| Reconstruir sin caché | `docker compose build --no-cache app` |
| Ver contenedores activos | `docker compose ps` |
| Acceder al contenedor app | `docker compose exec app sh` |
| Acceder a MySQL | `docker compose exec mysql mysql -u root bd_lolimsa` |

## Notas importantes

- La base de datos se crea vacía. Hibernate (`ddl-auto=update`) crea las tablas automáticamente al iniciar la app.
- Las imágenes subidas por usuarios se guardan en la carpeta `./uploads/` del proyecto (persisten aunque borres los contenedores).
- Si cambias el código fuente, solo necesitas repetir el paso 2 (`docker compose up --build -d`).
- En Windows, Docker Desktop debe estar ejecutándose. Si ves errores de conexión, abre Docker Desktop y espera a que el motor inicie.

## Solución de problemas

**Error: "port is already allocated"**
→ Otro proceso está usando el puerto 3309 o 8083. Cierra el programa correspondiente o cambia los puertos en `docker-compose.yml`.

**Error de conexión a MySQL**
→ Espera unos segundos y reintenta. La app tiene `depends_on: condition: service_healthy` pero MySQL puede tardar en inicializarse la primera vez.

**Error "no matching manifest for windows/amd64"**
→ Asegúrate de que Docker Desktop esté usando el motor Linux (no Windows containers). Haz clic derecho en el ícono de Docker → "Switch to Linux containers".

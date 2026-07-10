# Correcciones de Seguridad — ZAP Scan

**Fecha del scan:** 27 junio 2026  
**Herramienta:** ZAP 2.17.0 (Checkmarx)  
**URL:** `http://localhost:8083`

---

## Resumen

| Riesgo | Cantidad |
| ------ | -------- |
| Alto   | 0        |
| Medio  | 2        |
| Bajo   | 2        |
| Informativo | 4    |

---

## 1. [Medio] Cabecera Content Security Policy (CSP) no configurada

**Alertas:** 5 instancias (GET `/`, `/login`, `/register`, `/js/index.js`, `/css/index.css`)

**CWE:** 693

### Problema

El servidor no enviaba la cabecera HTTP `Content-Security-Policy`, lo que permite potenciales ataques XSS al no restringir las fuentes de contenido (scripts, estilos, fuentes, etc.).

### Solución implementada

En `SecurityConfig.java:57-66` se agregó una política CSP:

```java
.contentSecurityPolicy(csp -> csp
    .policyDirectives("default-src 'self'; " +
            "script-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://kit.fontawesome.com 'unsafe-inline'; " +
            "style-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com 'unsafe-inline'; " +
            "img-src 'self' data:; " +
            "font-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com data:; " +
            "connect-src 'self'; " +
            "frame-src 'self'; " +
            "object-src 'none'")
)
```

Directivas aplicadas:

| Directiva | Valor |
|-----------|-------|
| `default-src` | `'self'` |
| `script-src` | `'self'`, CDNs, `'unsafe-inline'` (necesario para Chart.js inline) |
| `style-src` | `'self'`, CDNs, `'unsafe-inline'` |
| `img-src` | `'self'`, `data:` |
| `font-src` | `'self'`, CDNs, `data:` |
| `connect-src` | `'self'` |
| `frame-src` | `'self'` |
| `object-src` | `'none'` |

### Verificación

```
$ curl -s -I http://localhost:8083 | findstr CSP
content-security-policy: default-src 'self'; script-src ...
```

---

## 2. [Medio] Falta atributo de integridad de recursos secundarios (SRI)

**Alertas:** 5 instancias (en páginas index, login, register y parciales)

**CWE:** 345

### Problema

Los recursos cargados desde CDNs externas (Bootstrap, Font Awesome, Bootstrap Icons, Chart.js) no tenían el atributo `integrity` ni `crossorigin`, lo que permite que un CDN comprometido sirva código malicioso sin que el navegador lo detecte.

### Solución implementada

Se agregaron los atributos `integrity` (hash SHA-384/512) y `crossorigin="anonymous"` a **todos** los `<link>` y `<script>` de CDN en los **35 templates** del proyecto.

Ejemplo:

```html
<!-- Antes -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Después -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
      crossorigin="anonymous">
```

Se estandarizaron las versiones de CDN para eliminar duplicados:

| Recurso | Versión estandarizada |
|---------|-----------------------|
| Bootstrap CSS | 5.3.8 |
| Bootstrap JS | 5.3.8 |
| Bootstrap Icons | 1.11.3 |
| Font Awesome | 6.5.0 |
| Chart.js | 4.4.7 |

Templates corregidos: `index.html`, `login.html`, `register.html`, y los 32 templates en las carpetas `gerente/`, `medicos/`, `pacientes/`.

---

## 3. [Bajo] Cookie JSESSIONID sin atributo SameSite

**Alertas:** 3 instancias

**CWE:** 1275

### Problema

La cookie de sesión `JSESSIONID` se enviaba sin el atributo `SameSite`, haciéndola vulnerable a ataques CSRF y solicitudes cross-site.

### Solución implementada

En `application.properties` se agregó:

```properties
server.servlet.session.cookie.same-site=lax
server.servlet.session.cookie.http-only=true
```

- `SameSite=Lax`: la cookie solo se envía en solicitudes de navegación de alto nivel (clics en enlaces), no en solicitudes de terceros. Previene CSRF sin romper la navegación normal.
- `HttpOnly`: la cookie no es accesible desde JavaScript (`document.cookie`), mitigando XSS.

> Nota: `Secure=true` se dejó comentado porque solo debe activarse bajo HTTPS.

### Verificación

```
Set-Cookie: JSESSIONID=...; Path=/; HttpOnly; SameSite=Lax
```

---

## 4. [Bajo] Inclusión de archivos fuente JavaScript entre dominios

**Alertas:** 5 instancias (index, login, register, etc.)

**CWE:** 829

### Problema

La aplicación carga JavaScript y CSS desde CDNs externas (cdn.jsdelivr.net, cdnjs.cloudflare.com, kit.fontawesome.com). Si un atacante compromete el CDN, puede inyectar scripts maliciosos.

### Solución implementada

Combinación de dos defensas:

1. **SRI** (detallado en el punto 2): los hashes `integrity` garantizan que el navegador solo ejecute el archivo exacto que se espera.

2. **CSP** (detallado en el punto 1): se restringieron las fuentes de script/style a solo los CDNs autorizados, bloqueando cualquier otro origen no listado.

Con ambas medidas, incluso si un CDN es comprometido, el navegador:
- Rechazará el archivo modificado (por SRI)
- Bloqueará cualquier recurso de un origen no listado en CSP

---

## 5. [Informativo] Atributo de elemento HTML controlable por el usuario (XSS potencial)

**Alertas:** 20 instancias

**CWE:** 20

### Diagnóstico

ZAP detectó que los valores enviados por el usuario en el formulario de registro (`/register`) se re-escriben en los atributos `value` de los `<input>` al hacer render del formulario con errores de validación. Thymeleaf escapa automáticamente la salida con `th:field`, pero ZAP lo marca como punto caliente para revisión manual.

En el código se usa `th:value="${...}"` que escapa correctamente el HTML, y los datos se persisten vía JPA (prepared statements), no hay concatenación de strings SQL. No se requiere acción correctiva adicional.

---

## 6. [Informativo] Otras alertas

| Alerta | Instancias | Motivo |
|--------|------------|--------|
| Aplicación Web Moderna | 2 | La app usa enlaces sin href tradicional. Informativo, no requiere cambio. |
| Petición de Autenticación Identificada | 2 | ZAP detectó el flujo de login. Informativo. |
| Respuesta de Gestión de Sesión Identificada | 3 | ZAP detectó la cookie JSESSIONID. Informativo. |

---

## Archivos modificados

| Archivo | Cambio |
|---------|--------|
| `src/main/java/com/example/clinicalolimsa/security/SecurityConfig.java` | Agregada cabecera CSP |
| `src/main/resources/application.properties` | Agregado `SameSite=Lax` y `HttpOnly` |
| `src/main/resources/templates/index.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/login.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/register.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentepanel.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentecitas/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentecompras/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentemedicamentos/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentemedicos/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentepaciente/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerenteproveedores/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentealmacen/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/gerente/gerentetipodemedicamentos/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/medicos/medicospanel.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/medicos/*.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/pacientes/pacientepanel.html` | SRI + crossorigin + versión estandarizada |
| `src/main/resources/templates/pacientes/*.html` | SRI + crossorigin + versión estandarizada |

**Total:** ~35 templates corregidos + 2 archivos de configuración.

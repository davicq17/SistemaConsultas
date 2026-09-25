# Documentación del Cliente Web (Angular) — Carpeta preparada

> Estado: **PENDIENTE**. Esta carpeta se prepara ahora para recibir la documentación cuando se ejecute la migración del cliente web a Angular. El cliente actual (`Frontend/` en HTML/CSS/JS vanilla) sigue siendo el vigente y la app móvil (`iubconsultas/`, ver `docs/movil/`) no se ve afectada por esa migración.

---

# Alcance de la migración (resumen)

- Reemplazar únicamente `Frontend/` por una SPA en Angular.
- Consumir la misma API REST (ver `docs/05-API.md` y Swagger); sin cambios en el backend salvo lo que pida la dockerización.
- Dockerizar API + cliente web Angular (ver Roadmap del `README.md`).

---

# Índice previsto

| Documento | Contenido |
|---------------------|-----------|
| [01-Plan-Migracion.md](01-Plan-Migracion.md) | Checklist pendiente de la migración |
| [02-Auditoria-Cliente-Web-Backend.md](02-Auditoria-Cliente-Web-Backend.md) | Inconsistencias verificadas web vs backend/BD (base para corregir en Angular) |
| `03-Arquitectura.md` (a crear) | Módulos, routing, guards, interceptores y servicios |
| `04-Convenciones.md` (a crear) | Estilo Angular, formularios reactivos, manejo de errores |
| `05-Docker.md` (a crear) | `Dockerfile` del web + `docker-compose` con la API |

---

# Regla de trabajo

Cuando inicie la migración (rama sugerida `feature/frontend-angular`):

1. Implementar el plan de `01-Plan-Migracion.md`, corrigiendo las inconsistencias de `02-Auditoria-Cliente-Web-Backend.md`.
2. Crear los documentos `03`, `04` y `05` siguiendo el formato de `docs/01-Arquitectura.md` y `docs/movil/`.
3. Actualizar el `README.md` (tecnologías, estructura y roadmap) y archivar la doc del cliente vanilla si se retira `Frontend/`.

# Documentación del Cliente Móvil

Cliente móvil nativo Android del **Sistema de Gestión de Consultas Académicas**, ubicado en `iubconsultas/`. Consume la API REST del backend (Kotlin + Spring Boot) y es un cliente independiente del cliente web (`Frontend/`): la futura migración del cliente web a Angular no lo afecta.

---

# Índice

| Documento | Descripción |
|-----------|-------------|
| [01-Arquitectura.md](01-Arquitectura.md) | Arquitectura MVVM, capas `data`/`ui` y flujo de una petición |
| [02-Convenciones.md](02-Convenciones.md) | Convenciones de nombres, estructura Screen/ViewModel/UiState y reglas de código |
| [03-Seguridad-Sesion.md](03-Seguridad-Sesion.md) | Autenticación JWT, interceptor, `SessionManager` y roles |
| [04-Navegacion-Roles.md](04-Navegacion-Roles.md) | Destinos, `NavHost` y homes por rol |
| [05-Integracion-API.md](05-Integracion-API.md) | Endpoints consumidos, DTOs, entornos (`BASE_URL`) y brechas |
| [06-Build-Despliegue.md](06-Build-Despliegue.md) | Requisitos, compilación, ejecución y limitaciones conocidas |

---

# Relación con el resto del proyecto

- Backend: `docs/01-Arquitectura.md` … `docs/06-ModeloDatos.md` (la app consume esa API).
- Cliente web actual: `Frontend/` (HTML/CSS/JS vanilla; será reemplazado por Angular, ver `docs/web/`).
- Cliente web futuro (Angular): `docs/web/` (carpeta preparada, sin implementar).
- Roadmap general: `README.md`.

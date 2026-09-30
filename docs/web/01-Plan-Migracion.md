# Plan de Migración del Cliente Web a Angular — Borrador pendiente

> Estado: **PENDIENTE de ejecución**. Checklist propuesto para cuando se apruebe la migración. Alcance limitado al cliente web (`Frontend/` → Angular); la app móvil (`iubconsultas/`) y el backend no cambian salvo lo indicado.

---

# 1. Precondiciones

- [ ] API estable en `develop` y contrato verificado en Swagger.
- [ ] Rama creada: `feature/frontend-angular`.
- [ ] Versión de Angular/LTS y estrategia de estilos acordadas por el equipo.

---

# 2. Checklist funcional (paridad con `Frontend/`)

- [ ] Login y registro contra `/auth` (guards por rol: Administrador, Docente, Estudiante).
- [ ] Interceptor JWT + redirección a login ante 401/403.
- [ ] CRUD de catálogos: usuarios, programas, módulos, sedes, bloques, recursos físicos.
- [ ] Solicitudes: crear, listar, `mis-solicitudes`, cambio de estado, asignación de recurso, reasignación de docente.
- [ ] Comentarios por solicitud y visualización de notificaciones.
- [ ] Formularios reactivos con validación equivalente a Bean Validation del backend.
- [ ] Estados de carga/error en todas las vistas.

---

# 3. Checklist técnico

- [ ] Arquitectura por módulos con lazy-loading; servicios tipados por recurso.
- [ ] Variables de entorno por perfil (`environment.ts` / `environment.prod.ts`) apuntando a la API dockerizada.
- [ ] `Dockerfile` multistage (build + Nginx) del cliente web.
- [ ] `docker-compose` que levanta API + web (+ MySQL) con variables externalizadas.
- [ ] Documentar en `docs/web/02-Arquitectura.md`, `03-Convenciones.md` y `04-Docker.md`.

---

# 4. Criterios de aceptación

- [ ] Paridad funcional verificada contra el cliente vanilla antes de retirarlo.
- [ ] `docker compose up` levanta el sistema completo con un solo comando.
- [ ] `README.md` y `docs/web/` actualizados; roadmap marcado como completado.

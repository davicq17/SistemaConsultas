# AGENTS.md — SistemaConsultas

Backend: Kotlin 2.2.21 + Spring Boot 4.0.7, Java 17 toolchain, Gradle Kotlin DSL; root project `consultas`. No CI, no backend lint/formatter, no `opencode.json`; the Angular client has Prettier (`.prettierrc`).

## Run / verify (backend, repo root)

```bash
./gradlew bootRun      # API on 0.0.0.0:8080; Swagger: http://localhost:8080/swagger-ui/index.html
./gradlew test         # only contextLoads, but boots the full Spring context
./gradlew test --tests "co.edu.iub.sistemaconsultas.SistemaConsultasApplicationTests"
./gradlew build
```

- `.env.example` is only a template: the app does **not** load `.env`. Export vars in the shell or set them in the IDE run config.
- `JWT_SECRET` is required (no default) and must be at least 32 bytes for HS256. MySQL must be running even for `test`.
- `application.yaml` defaults to `jdbc:mysql://localhost:3306/sistema_consultas` and `ddl-auto: update`.

## Architecture (enforced per `docs/02-Convenciones.md`)

Layered, one direction only: `controller → service → repository → MySQL` under `src/main/kotlin/co.edu.iub.sistemaconsultas/` (`config, controller, dto, exception, mapper, model, repository, service, service/impl, util`).

- Business logic **only** in `*ServiceImpl` (`service/impl/`). Controllers are thin: `@Valid` DTO → delegate to service → return DTO. Never touch repositories/SQL from controllers.
- Never return JPA entities. Every operation uses Request/Response DTOs; conversions live in `mapper/` as Kotlin extension functions, not duplicated in services. Mapper files are `*Mapper.kt` except `SolicitudConsulta.kt`.
- Logical delete via `activo` field — no physical deletes without justification.
- Errors: throw `ResourceNotFoundException` / `BadRequestException`, handled by `GlobalExceptionHandler`. Never return null for errors.
- New endpoints need OpenAPI annotations; keep Swagger in sync.
- Naming: `*Controller`, `*Service` + `*ServiceImpl`, `*Repository`, `*Request`/`*Response`, singular entities. Note: code abbreviates `ProgAcademico*` (not `ProgramaAcademico*`); DTO subpackages use camelCase (`programaAcademico`, `recursoFisico`).

## Auth / API surface

- Stateless JWT: `JwtAuthenticationFilter` + `SecurityConfig`; `PasswordEncoder` (BCrypt) in `SecurityBeansConfig`. There is **no** `@PreAuthorize`/role matcher — role rules are enforced manually in services (e.g. only `ESTUDIANTE` may create a solicitud).
- Actual `permitAll` in `SecurityConfig`: `/auth/{login,register,forgot-password,reset-password}`, **all methods on `/solicitudes-consultas`**, `GET /programas`, Swagger, `/error`. Everything else needs `Authorization: Bearer <JWT>`.
- Gotcha: `POST /solicitudes-consultas` is open at the filter level but its service reads the authenticated principal; an anonymous call can surface as 500, not 401. `docs/web/02-Auditoria...` (W-14) expects this tightened.
- No `GET /solicitudes-consultas/mis-solicitudes` handler exists on `develop`/the current branch, despite docs and `listarMisSolicitudes()` in the service interface. Don't document it as available without adding the controller route.
- `/reportes` exists **only** on unmerged `feat/reportes` — do not reference it on `develop`.
- Other prefixes: `/usuarios`, `/programas`, `/modulos`, `/sedes`, `/bloques`, `/recursos-fisicos`, `/comentarios`, `/notificaciones`. Solicitud sub-routes: `PATCH /{id}/estado`, `/{id}/recurso-fisico`, `/{id}/docente`.
- Operational gotchas: `EmailService` is a stub that prints reset-password tokens to stdout (no SMTP); `JwtServiceImpl.generarToken` currently prints the JWT secret. Don't add secret logging; use the console to test password recovery.

## Clients (three, don't confuse)

- `Frontend/` (tracked): legacy vanilla HTML/CSS/JS per role. Still the functional web client.
- `FRONTEND/` (case-sensitive, tracked on `feat/frontend-agular`): Angular 22.2 standalone + Tailwind + pnpm. Use `cd FRONTEND && pnpm install && pnpm start` (:4200), `pnpm build`, `pnpm test` (Vitest via `ng test`; no e2e). Use pnpm, not npm (`pnpm-lock.yaml`).
- `FRONTEND/` is still scaffolding: `app.routes.ts` is empty and most `features/` folders only contain `.gitkeep`. `docs/web/03-Arquitectura.md` and `04-Convenciones.md` describe the target structure. Environment files are misspelled `enviroment.ts` / `enviroment.prod.ts` (not `environment.ts`) and are currently untracked — import the real path.
- Actual branch is `feat/frontend-agular` (sic); docs say `feature/frontend-angular`.
- `iubconsultas/`: independent Android/Gradle project (own `settings.gradle.kts`, `rootProject.name = "iubconsultas"`; Java 11 target, minSdk 24). Run from inside it: `cd iubconsultas && ./gradlew assembleDebug`. Backend URL is hardcoded in `data/remote/RetrofitClient.kt`: `BASE_URL = SERVER_WIFI`, currently the placeholder `http://tuIP:8080/` — set a real IP, or use `SERVER_EMULATOR` (`http://10.0.2.2:8080/`) / `SERVER_ADB` (`http://localhost:8080/`). `usesCleartextTraffic` is dev-only.

## Git workflow (`docs/03-FlujoGit.md`)

`main` = stable, `develop` = integration base. Branch from `develop` (`feature/*` per docs, but history uses `feat/*`); `bugfix|hotfix|docs|security/*` when applicable. PR to `develop` must: compile, exercise endpoints via Swagger/Postman, have no conflicts, update `docs/` + OpenAPI if behavior changed, contain no credentials. Commits: `feat|fix|refactor|docs|test|chore: ...`.

Authoritative detail lives in `docs/`: `01-Arquitectura.md`, `02-Convenciones.md`, `03-FlujoGit.md`, `04-Seguridad.md`, `05-API.md`, `06-ModeloDatos.md`, `web/` (Angular), `movil/06-Build-Despliegue.md`. Trust controllers/config over `README.md` and docs roadmap prose: both claim features (e.g. `mis-solicitudes`, completed Angular migration) that the code does not yet have.

# AGENTS.md — SistemaConsultas

Backend: Kotlin + Spring Boot 4.0.7, Java 17 toolchain, Gradle Kotlin DSL. Root project name is `consultas` (`settings.gradle.kts`). No CI, no lint/formatter config, no `opencode.json`.

## Run / verify (backend, repo root)

```bash
./gradlew bootRun      # API on 0.0.0.0:8080; Swagger: http://localhost:8080/swagger-ui/index.html
./gradlew test         # only SistemaConsultasApplicationTests.contextLoads (needs live MySQL + env)
./gradlew build
```

Requires MySQL running + env from `.env.example`: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DDL_AUTO` (use `update`), `JWT_SECRET` (**required, no default, min 32 chars — app fails without it**), `JWT_EXPIRATION_MINUTES` (default 30). `src/main/resources/application.yaml` reads all via `${...}`.

## Architecture (enforced per `docs/02-Convenciones.md`)

Layered, one direction only: `controller → service → repository → MySQL` under `src/main/kotlin/co.edu.iub.sistemaconsultas/` (`config, controller, dto, exception, mapper, model, repository, service, service/impl, util`).

- Business logic **only** in `*ServiceImpl` (`service/impl/`). Controllers are thin: `@Valid` DTO → delegate to service → return DTO. Never touch repositories/SQL from controllers.
- Never return JPA entities. Every operation uses Request/Response DTOs; conversions live in `mapper/` as Kotlin extension functions, not duplicated in services.
- Logical delete via `activo` field — no physical deletes without justification.
- Errors: throw `ResourceNotFoundException` / `BadRequestException`, handled by `GlobalExceptionHandler`. Never return null for errors.
- New endpoints need OpenAPI annotations; keep Swagger in sync.
- Naming: `*Controller`, `*Service` + `*ServiceImpl`, `*Repository`, `*Request`/`*Response`, singular entities. Note: code abbreviates `ProgAcademico*` (not `ProgramaAcademico*`).

## Auth / API surface

- Stateless JWT. Public routes only: `/auth/login`, `/auth/register`, `/auth/forgot-password`, `/auth/reset-password`. Everything else needs `Authorization: Bearer <JWT>`.
- `PasswordEncoder` (BCrypt) lives in `SecurityBeansConfig`; filter chain in `SecurityConfig`.
- Resource prefixes on `develop`: `/usuarios`, `/programas`, `/modulos`, `/sedes`, `/bloques`, `/recursos-fisicos`, `/solicitudes-consultas` (incl. `mis-solicitudes`, cambio de estado, asignación de recurso, reasignación de docente), `/comentarios`, `/notificaciones`. `/reportes` exists **only** on unmerged `feat/reportes` — do not reference it on `develop`.

## Clients (three, don't confuse)

- `Frontend/` (tracked): legacy vanilla HTML/CSS/JS per role. `FRONTEND/` (untracked, branch `feat/frontend-angular`): Angular 22 + pnpm (`cd FRONTEND && pnpm install && pnpm start` / `ng serve` on :4200). Case-sensitive — they are different clients.
- `iubconsultas/`: independent Android/Gradle project (own `settings.gradle.kts`, `rootProject.name = "iubconsultas"`). Run from inside it: `cd iubconsultas && ./gradlew assembleDebug`. Backend URL is hardcoded in `data/remote/RetrofitClient.kt` (`BASE_URL = SERVER_WIFI` = `http://192.168.1.6:8080/`; use `SERVER_EMULATOR` `10.0.2.2` for emulator, `SERVER_ADB` for ADB). `usesCleartextTraffic` is dev-only.

## Git workflow (`docs/03-FlujoGit.md`)

`main` = stable, `develop` = integration base. Branch from `develop` as `feature/*` (`bugfix|hotfix|docs|security/*` when applicable). PR to `develop` must: compile, exercise endpoints via Swagger/Postman, have no conflicts, update `docs/` + OpenAPI if behavior changed, contain no credentials. Commits: `feat|fix|refactor|docs|test|chore: ...`.

Authoritative detail lives in `docs/`: `01-Arquitectura.md`, `02-Convenciones.md`, `03-FlujoGit.md`, `04-Seguridad.md`, `05-API.md`, `movil/06-Build-Despliegue.md`. Trust them + Gradle/config files over `README.md` roadmap prose.

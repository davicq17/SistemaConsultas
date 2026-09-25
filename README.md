# Sistema de Gestión de Consultas Académicas

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.7-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.5.1-02303A?logo=gradle&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-Enabled-6DB33F?logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-0.12.6-000000?logo=jsonwebtokens&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-2.8.10-85EA2D?logo=swagger&logoColor=black)
![Architecture](https://img.shields.io/badge/Architecture-Layered-blue)
![API](https://img.shields.io/badge/API-REST-blueviolet)
![Documentation](https://img.shields.io/badge/Documentation-Complete-brightgreen)
![Status](https://img.shields.io/badge/Status-In_Development-orange)
![License](https://img.shields.io/badge/License-Educational_Project-lightgrey)

Backend desarrollado con **Kotlin + Spring Boot** para la gestión de consultas académicas entre estudiantes, docentes y administradores.

El proyecto fue diseñado siguiendo una arquitectura por capas, aplicando buenas prácticas de desarrollo backend, principios de separación de responsabilidades y una estrategia de autenticación basada en JWT.

---

# Características principales

Actualmente el sistema implementa las siguientes funcionalidades:

## Backend (API REST)
- Autenticación mediante JWT.
- Registro de usuarios.
- Inicio de sesión.
- Recuperación segura de contraseña.
- CRUD de Usuarios.
- CRUD de Programas Académicos.
- CRUD de Módulos.
- CRUD de Sedes.
- CRUD de Bloques.
- CRUD de Recursos Físicos.
- Módulo de Solicitudes de Consulta (creación, actualización, cambio de estado, asignación de recurso físico, reasignación de docente y consulta de `mis-solicitudes` por usuario).
- Módulo de Comentarios por solicitud.
- Módulo de Notificaciones automáticas por cambio de estado.
- Historial de actividades mediante EventoSolicitud (`TipoEvento`).
- Bean Validation.
- Manejo global de excepciones.
- Documentación interactiva con Swagger/OpenAPI.
- Eliminación lógica de registros administrativos.
- Arquitectura por capas.
- Documentación técnica completa.

## Frontend Web (HTML/CSS/JS vanilla)
- Vistas por rol: Administrador, Docente y Estudiante.
- Login y registro consumiendo la API (`/auth`).
- Panel de administración con gestión de catálogos y solicitudes.

## App móvil Android (Jetpack Compose)
- Cliente móvil nativo en `iubconsultas/` que consume la API (login, usuarios, catálogos, solicitudes, comentarios).

> Nota: el módulo de Reportes administrativos (entidad `Reporte`, `TipoReporte`, generación de PDF) se encuentra desarrollado en la rama `feat/reportes` y aún no está integrado a `develop`.

---

# Tecnologías utilizadas

## Backend
- Kotlin
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT
- BCrypt
- MySQL
- Gradle Kotlin DSL
- Java 17
- Swagger / OpenAPI
- IntelliJ IDEA

## Frontend Web (actual)
- HTML, CSS y JavaScript vanilla (carpeta `Frontend/`)

## App móvil
- Android nativo con Kotlin y Jetpack Compose (módulo `iubconsultas/`)

## Cliente web objetivo (roadmap, solo reemplaza a `Frontend/`)
- Angular (migración prevista únicamente del cliente web actual; la app móvil `iubconsultas/` se mantiene sin cambios, ver Roadmap)

---

# Arquitectura

El proyecto implementa una arquitectura por capas con el objetivo de mantener una adecuada separación de responsabilidades.

```text
Controller

↓

Service

↓

Repository

↓

JPA

↓

MySQL
```

La lógica de negocio se implementa exclusivamente en la capa de servicios, manteniendo los Controllers ligeros y enfocados únicamente en recibir y responder solicitudes HTTP.

---

# Estructura del proyecto

```text
SistemaConsultas
├── src/main/kotlin/co.edu.iub.sistemaconsultas
│   ├── config
│   ├── controller
│   ├── dto
│   ├── exception
│   ├── mapper
│   ├── model
│   ├── repository
│   ├── service
│   │   └── impl
│   └── util
├── Frontend/                # Cliente web actual (HTML/CSS/JS por rol)
│   ├── Administrador.html
│   ├── Docente.html
│   ├── Estudiante.html
│   ├── index.html / registro.html
│   └── assets/{css,javascript,img}
├── iubconsultas/            # App móvil Android (Jetpack Compose)
├── docs/                    # Documentación técnica
└── build.gradle.kts
```

---

# Requisitos

Para ejecutar el proyecto se requiere:

- Java 17
- Gradle
- MySQL
- IntelliJ IDEA (recomendado)

---

# Instalación

## 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
```

## 2. Ingresar al proyecto

```bash
cd SistemaConsultas
```

## 3. Configurar las variables de entorno

Copiar el archivo:

```text
.env.example
```

y configurar los valores correspondientes al entorno local.

## 4. Ejecutar la aplicación

Desde IntelliJ IDEA.

O utilizando Gradle:

```bash
./gradlew bootRun
```

---

# Variables de entorno

El proyecto utiliza variables de entorno para evitar almacenar información sensible dentro del código fuente.

Variables requeridas:

- DB_URL
- DB_USERNAME
- DB_PASSWORD
- DDL_AUTO
- JWT_SECRET
- JWT_EXPIRATION_MINUTES

Consultar el archivo `.env.example` para conocer el formato esperado.

---

# Documentación

La documentación técnica del proyecto se encuentra organizada en la carpeta **docs**, separada por cliente: backend (API), app móvil Android y futuro cliente web Angular.

## Backend (API)

| Documento                                     | Descripción                                |
|-----------------------------------------------|--------------------------------------------|
| [01-Arquitectura.md](docs/01-Arquitectura.md) | Arquitectura general del sistema           |
| [02-Convenciones.md](docs/02-Convenciones.md) | Convenciones de desarrollo                 |
| [03-FlujoGit.md](docs/03-FlujoGit.md)         | Estrategia de trabajo con Git              |
| [04-Seguridad.md](docs/04-Seguridad.md)       | Arquitectura e implementación de seguridad |
| [05-API.md](docs/05-API.md)                   | Convenciones generales de la API REST      |
| [06-ModeloDatos.md](docs/06-ModeloDatos.md)   | Dominio y entidades de trabajo             |

## Cliente móvil Android (`iubconsultas/`)

| Documento                                         | Descripción                              |
|---------------------------------------------------|------------------------------------------|
| [00-Indice.md](docs/movil/00-Indice.md)           | Índice y mapa de la doc móvil            |
| [01-Arquitectura.md](docs/movil/01-Arquitectura.md) | Arquitectura MVVM y flujo de petición  |
| [02-Convenciones.md](docs/movil/02-Convenciones.md) | Convenciones de código móvil           |
| [03-Seguridad-Sesion.md](docs/movil/03-Seguridad-Sesion.md) | JWT, sesión y roles en la app   |
| [04-Navegacion-Roles.md](docs/movil/04-Navegacion-Roles.md) | Destinos y homes por rol       |
| [05-Integracion-API.md](docs/movil/05-Integracion-API.md) | Endpoints consumidos y brechas |
| [06-Build-Despliegue.md](docs/movil/06-Build-Despliegue.md) | Compilación y conexión al backend |

## Cliente web futuro (Angular, pendiente)

| Documento                              | Descripción                                              |
|----------------------------------------|----------------------------------------------------------|
| [README.md](docs/web/README.md)        | Alcance e índice previsto de la doc web                  |
| [01-Plan-Migracion.md](docs/web/01-Plan-Migracion.md) | Checklist pendiente de la migración a Angular |
| [02-Auditoria-Cliente-Web-Backend.md](docs/web/02-Auditoria-Cliente-Web-Backend.md) | Inconsistencias web vs backend/BD a corregir en Angular |

---

# Estado del proyecto

## Funcionalidades implementadas (rama `develop`)

- Autenticación mediante JWT (login, registro, recuperación de contraseña).
- CRUD de Usuarios.
- CRUD de Programas Académicos (`/programas`).
- CRUD de Módulos (`/modulos`).
- CRUD de Sedes (`/sedes`).
- CRUD de Bloques (`/bloques`).
- CRUD de Recursos Físicos (`/recursos-fisicos`).
- Módulo de Solicitudes de Consulta (`/solicitudes-consultas`): creación, actualización, cambio de estado (`PENDIENTE, EN_PROCESO, RESUELTA, RECHAZADA, CANCELADA`), asignación de recurso físico, reasignación de docente y endpoint `mis-solicitudes`.
- Módulo de Comentarios (`/comentarios`).
- Módulo de Notificaciones (`/notificaciones`) generadas ante cambios de estado.
- Historial de actividades (EventoSolicitud con `TipoEvento`: `CREACION, RECHAZO, CAMBIO_ESTADO, CANCELACION, ASIGNACION_RECURSO, REASIGNACION_DOCENTE, COMENTARIO, CAMBIO_AGENDAMIENTO`).
- Bean Validation.
- Swagger/OpenAPI.
- Manejo global de excepciones.
- Eliminación lógica en entidades administrativas.
- Frontend Web por roles (Administrador, Docente, Estudiante).
- App móvil Android que consume la API.

## Funcionalidades pendientes / en curso

- Integrar a `develop` el módulo de Reportes administrativos (actualmente en la rama `feat/reportes`: `ReporteController`, `TipoReporte`, proyecciones y generación de PDF).
- Restricción para impedir eliminar Programas Académicos con usuarios activos asociados.
- Pruebas unitarias con JUnit y Mockito (actualmente solo existe `SistemaConsultasApplicationTests` de contexto).
- Migración del cliente web actual (`Frontend/` en HTML/CSS/JS vanilla) a Angular. Alcance limitado al cliente web: la app móvil `iubconsultas/` sigue existiendo y no se ve afectada (ver Roadmap).

---

# Roadmap

## Completado

- Seguridad con Spring Security.
- JWT.
- BCrypt.
- CRUD Usuarios.
- CRUD Programas Académicos.
- CRUD Módulos.
- CRUD Sedes, Bloques y Recursos Físicos.
- Módulo Solicitudes de Consulta (incluye `mis-solicitudes`, cambio de estado, asignación de recurso y reasignación de docente).
- Módulo Comentarios y Notificaciones.
- Historial de actividades (EventoSolicitud).
- Recuperación de contraseña.
- Bean Validation.
- Swagger/OpenAPI.
- Documentación técnica.
- Frontend Web por roles (HTML/CSS/JS).
- App móvil Android (Jetpack Compose) que consume la API.

## Próximas mejoras

- Migrar el cliente web a Angular (solo `Frontend/`, sin afectar a la app móvil): reemplazar el cliente actual (HTML/CSS/JS vanilla) por una SPA en Angular con arquitectura por módulos/lazy-loading, guards por rol (Administrador/Docente/Estudiante), interceptores JWT, servicios por recurso (`auth, usuarios, programas, módulos, sedes, bloques, recursos-físicos, solicitudes, comentarios, notificaciones`), formularios reactivos con validación, y consumo tipado de la API documentada en Swagger. La app móvil Android (`iubconsultas/`) se mantiene como cliente independiente.
- Dockerizar el proyecto entero: API (Spring Boot) y cliente web Angular con `Dockerfile` por componente y `docker-compose` para levantar API + web (+ MySQL), con variables de entorno externalizadas y perfiles dev/prod.
- Integrar el módulo de Reportes administrativos desde la rama `feat/reportes` (incluye `TipoReporte`, proyecciones y exportación a PDF).
- Pruebas unitarias y mayor cobertura (JUnit + Mockito) en Services y Controllers.
- Optimización de reglas de negocio (p. ej. validación de eliminación de Programas con usuarios activos, disponibilidad de recursos físicos).
- Paginación, filtrado y ordenamiento en listados.
- Mejoras de seguridad (Refresh Tokens, revocación de JWT, MFA, límite de intentos de login, auditoría).

---

# Integrantes

- David Carrillo
- Martin Clavijo

---

# Autor

Proyecto desarrollado con fines académicos como parte del proceso de formación en desarrollo Backend utilizando Kotlin y Spring Boot, aplicando buenas prácticas de arquitectura de software y desarrollo profesional.

---

# Licencia

Este proyecto tiene fines exclusivamente académicos.

Su uso, modificación o distribución deberá respetar las políticas establecidas por la institución educativa y los autores del proyecto.

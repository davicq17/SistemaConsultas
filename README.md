# Sistema de Gestión de Consultas Académicas

Backend desarrollado con **Kotlin + Spring Boot** para la gestión de consultas académicas entre estudiantes, docentes y administradores.

El proyecto fue diseñado siguiendo una arquitectura por capas, aplicando buenas prácticas de desarrollo backend, principios de separación de responsabilidades y una estrategia de autenticación basada en JWT.

---

# Características principales

Actualmente el sistema implementa las siguientes funcionalidades:

- Autenticación mediante JWT.
- Registro de usuarios.
- Inicio de sesión.
- Recuperación segura de contraseña.
- CRUD de Usuarios.
- CRUD de Programas Académicos.
- CRUD de Módulos.
- Bean Validation.
- Manejo global de excepciones.
- Documentación interactiva con Swagger/OpenAPI.
- Eliminación lógica de registros.
- Arquitectura por capas.
- Documentación técnica completa.

---

# Tecnologías utilizadas

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
src
└── main
    └── kotlin
        └── co.edu.iub.sistemaconsultas
            ├── config
            ├── controller
            ├── dto
            ├── exception
            ├── mapper
            ├── model
            ├── repository
            ├── service
            │   └── impl
            └── util
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

La documentación técnica del proyecto se encuentra organizada en la carpeta **docs**.

| Documento                                     | Descripción |
|-----------------------------------------------|-------------|
| [01-Arquitectura.md](docs/01-Arquitectura.md) | Arquitectura general del sistema |
| [02-Convenciones.md](docs/02-Convenciones.md) | Convenciones de desarrollo |
| [03-FlujoGit.md](docs/03-FlujoGit.md)         | Estrategia de trabajo con Git |
| [04-Seguridad.md](docs/04-Seguridad.md)       | Arquitectura e implementación de seguridad |
| [05-API.md](docs/05-API.md)                   | Convenciones generales de la API REST |

---

# Estado del proyecto

## Funcionalidades implementadas

- Autenticación mediante JWT.
- Registro de usuarios.
- Inicio de sesión.
- Recuperación de contraseña.
- CRUD de Usuarios.
- CRUD de Programas Académicos.
- CRUD de Módulos.
- Bean Validation.
- Swagger/OpenAPI.
- Manejo global de excepciones.
- Eliminación lógica.

## Funcionalidades pendientes

- Integración del módulo de Solicitudes de Consulta.
- Restricción para impedir eliminar Programas Académicos con usuarios activos asociados.
- Pruebas unitarias con JUnit y Mockito.

---

# Roadmap

## Completado

- Seguridad con Spring Security.
- JWT.
- BCrypt.
- CRUD Usuarios.
- CRUD Programas Académicos.
- CRUD Módulos.
- Recuperación de contraseña.
- Bean Validation.
- Swagger/OpenAPI.
- Documentación técnica.

## Próximas mejoras

- Integración de Solicitudes de Consulta.
- Pruebas unitarias.
- Mayor cobertura de pruebas.
- Optimización de reglas de negocio.
- Mejoras de seguridad (Refresh Tokens, MFA, auditoría).

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
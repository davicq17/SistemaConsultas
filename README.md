# Sistema de Gestión de Consultas Académicas

## Descripción

Sistema backend desarrollado con **Kotlin + Spring Boot** para la gestión de consultas académicas entre estudiantes, docentes y administradores.

Este proyecto corresponde a la migración de una aplicación originalmente desarrollada en Flask (Python), rediseñando completamente la arquitectura para aplicar buenas prácticas de desarrollo backend utilizando Spring Boot.

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
- IntelliJ IDEA

---

# Arquitectura

El proyecto sigue una arquitectura por capas.

```
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

La lógica de negocio se implementa exclusivamente en la capa de servicios.

---

# Estructura del proyecto

```
src
└── main
    └── kotlin
        └── co.edu.iub.sistemaconsultas
            ├── config
            ├── controller
            ├── dto
            ├── exception
            ├── model
            ├── repository
            ├── service
            │   └── impl
            └── util
```

---

# Variables de entorno

El proyecto utiliza variables de entorno para evitar almacenar información sensible dentro del código fuente.

Variables necesarias:

- DB_URL
- DB_USERNAME
- DB_PASSWORD
- DDL_AUTO
- JWT_SECRET
- JWT_EXPIRATION_MINUTES

Se incluye un archivo `.env.example` con la configuración de referencia.

---

# Estado actual del proyecto

Actualmente se encuentra implementado:

- Autenticación mediante JWT.
- Registro de usuarios.
- Inicio de sesión.
- Encriptación de contraseñas con BCrypt.
- Persistencia con Spring Data JPA.
- Configuración mediante variables de entorno.

Próximamente:

- Filtro JWT.
- Protección completa de la API.
- CRUD de Usuarios.
- CRUD de Programas Académicos.
- CRUD de Módulos.
- CRUD de Solicitudes.
- CRUD de Consultas.

---

# Integrantes

- David Carrillo
- Martin Clavijo
- (Pendiente)

---

# Autor

Proyecto desarrollado con fines académicos aplicando buenas prácticas de desarrollo backend utilizando Kotlin y Spring Boot.
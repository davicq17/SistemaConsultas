# Convenciones del Proyecto

## Arquitectura

El proyecto sigue una arquitectura por capas:

Controller

↓

Service

↓

Repository

↓

Base de Datos

La lógica de negocio nunca debe implementarse en los Controllers.

---

## Nombres de clases

Controllers

UsuarioController

ProgramaAcademicoController

ModuloController

Services

UsuarioService

ProgramaAcademicoService

Repositories

UsuarioRepository

ProgramaAcademicoRepository

DTOs

LoginRequest

RegistroUsuarioRequest

UsuarioResponse

---

## Convenciones de código

- Una clase por archivo.
- Nombres de clases en PascalCase.
- Variables y funciones en camelCase.
- Evitar código duplicado.
- Utilizar inyección de dependencias mediante constructor.
- Mantener los Controllers lo más ligeros posible.

---

## Commits

Los mensajes deben describir claramente el cambio realizado.

Ejemplos:

feat: implementar autenticación JWT

feat: crear UsuarioController

fix: corregir validación de login

docs: actualizar documentación

refactor: reorganizar servicios

---

## Buenas prácticas

- No subir credenciales al repositorio.
- Mantener actualizado `.env.example`.
- Probar antes de hacer Push.
- Crear Pull Request antes de integrar cambios.
- Documentar nuevas funcionalidades cuando sea necesario.
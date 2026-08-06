# Convenciones del Proyecto

## 1. Objetivo

Este documento define las convenciones de desarrollo utilizadas en el proyecto **Sistema de Gestión de Consultas Académicas**.

Su objetivo es mantener un código consistente, legible y fácil de mantener, permitiendo que cualquier integrante del equipo pueda comprender la estructura del proyecto y desarrollar nuevas funcionalidades siguiendo los mismos criterios.

Todas las nuevas implementaciones deberán respetar estas convenciones.

---

# 2. Arquitectura

El proyecto sigue una arquitectura por capas.

```text
Cliente

↓

Controller

↓

Service

↓

Repository

↓

MySQL
```

Cada capa tiene una única responsabilidad.

- Los **Controllers** reciben las peticiones HTTP.
- Los **Services** contienen la lógica del negocio.
- Los **Repositories** interactúan con la base de datos.
- Las entidades representan el modelo persistente.

La lógica de negocio nunca debe implementarse en los Controllers.

---

# 3. Organización del proyecto

La estructura oficial del proyecto es la siguiente:

```text
co.edu.iub.sistemaconsultas

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

Cada paquete tiene una responsabilidad claramente definida y no debe utilizarse para almacenar clases que pertenezcan a otra capa.

---

# 4. Convenciones de nombres

## Controllers

Todos los Controllers deberán finalizar con el sufijo:

```text
Controller
```

Ejemplos:

```text
UsuarioController

ProgramaAcademicoController

ModuloController

AuthController
```

---

## Services

Las interfaces deberán finalizar con:

```text
Service
```

Ejemplos:

```text
UsuarioService

ProgramaAcademicoService

ModuloService
```

Las implementaciones deberán finalizar con:

```text
ServiceImpl
```

Ejemplos:

```text
UsuarioServiceImpl

ProgramaAcademicoServiceImpl

ModuloServiceImpl
```

---

## Repository

Todos los repositorios deberán finalizar con:

```text
Repository
```

Ejemplos:

```text
UsuarioRepository

ModuloRepository

ProgramaAcademicoRepository
```

---

## DTO

Los DTO deberán indicar claramente su propósito.

Para solicitudes:

```text
LoginRequest

RegistroUsuarioRequest

UpdateUsuarioRequest

ForgotPasswordRequest
```

Para respuestas:

```text
UsuarioResponse

LoginResponse

ProgramaAcademicoResponse
```

---

## Entities

Las entidades utilizarán nombres en singular.

Ejemplos:

```text
Usuario

Modulo

ProgramaAcademico

PasswordResetToken
```

---

# 5. Convenciones de código

Se deberán seguir las siguientes reglas generales.

- Una clase por archivo.
- Utilizar nombres descriptivos.
- Evitar abreviaturas innecesarias.
- Mantener métodos pequeños y con una única responsabilidad.
- Evitar código duplicado.
- Favorecer la reutilización mediante funciones y componentes comunes.
- Utilizar inyección de dependencias mediante constructor.
- Declarar dependencias como `private val` siempre que sea posible.

---

# 6. Controllers

Los Controllers representan la capa de entrada de la API.

Sus responsabilidades son:

- Recibir solicitudes HTTP.
- Validar DTO mediante `@Valid`.
- Delegar la ejecución al Service.
- Construir la respuesta HTTP.

Los Controllers **NO** deben:

- implementar reglas de negocio;
- acceder directamente a los Repository;
- realizar consultas SQL;
- contener validaciones complejas.

Los Controllers deben mantenerse lo más ligeros posible.

---

# 7. Services

Toda la lógica de negocio deberá implementarse en `ServiceImpl`.

Los Services pueden:

- validar reglas del negocio;
- consultar múltiples Repository;
- lanzar excepciones personalizadas;
- utilizar Mapper para transformar entidades.

Los Services no deben:

- construir respuestas HTTP;
- acceder al contexto web;
- depender de clases del Controller.

---

# 8. Repository

Los Repository representan la capa de acceso a datos.

Siempre que sea posible deberán utilizar los métodos proporcionados por Spring Data JPA.

Las consultas personalizadas únicamente deberán implementarse cuando los métodos derivados no sean suficientes.

Los Repository no deben contener reglas de negocio.

---

# 9. DTO

Toda comunicación entre cliente y servidor deberá realizarse mediante DTO.

Nunca se retornarán directamente entidades JPA.

Cada operación utilizará:

- Request DTO
- Response DTO

Esto permite desacoplar el modelo de persistencia de la API pública.

---

# 10. Mapper

Las conversiones entre entidades y DTO deberán centralizarse en la carpeta `mapper`.

Se utilizarán funciones de extensión de Kotlin para mantener un código limpio y reutilizable.

No se recomienda duplicar conversiones dentro de los Services.

---

# 11. Bean Validation

Las validaciones de formato deberán implementarse utilizando Bean Validation.

Ejemplos:

```java
@NotBlank

@NotNull

@Email

@Size
```

Los Controllers deberán utilizar `@Valid` para activar automáticamente las validaciones.

Las reglas de negocio seguirán implementándose en los Services.

---

# 12. Manejo de excepciones

Los errores deberán manejarse mediante excepciones personalizadas.

Actualmente el proyecto utiliza:

- ResourceNotFoundException
- BadRequestException

Todas las excepciones son procesadas por `GlobalExceptionHandler`.

No se deberán retornar valores `null` para indicar errores.

---

# 13. Seguridad

La autenticación se implementa mediante JWT.

Las contraseñas deberán almacenarse utilizando BCrypt.

Nunca deberán almacenarse contraseñas en texto plano.

Las rutas públicas son:

```text
/auth/login

/auth/register

/auth/forgot-password

/auth/reset-password
```

El resto de endpoints requieren autenticación.

---

# 14. Swagger / OpenAPI

Todo endpoint nuevo deberá documentarse utilizando las anotaciones de OpenAPI.

La documentación deberá mantenerse sincronizada con el código.

Los DTO utilizados por los endpoints también deberán documentarse cuando sea necesario.

---

# 15. Eliminación lógica

Las entidades principales utilizan eliminación lógica mediante el atributo:

```text
activo
```

No deberán realizarse eliminaciones físicas salvo que exista una justificación técnica.

Este mecanismo permite conservar el historial de información.

---

# 16. Convenciones para Git

Las ramas deberán seguir la siguiente estructura:

```text
feature/nombre-funcionalidad

bugfix/nombre-error

hotfix/nombre-error

docs/nombre-documento
```

Ejemplos:

```text
feature/usuarios

feature/programa-academico

feature/modulo

feature/recuperar-password

security/bean-validation
```

---

# 17. Convenciones para Commits

Los mensajes deberán describir claramente el cambio realizado.

| Tipo | Descripción | Ejemplo |
|------|-------------|----------|
| feat | Nueva funcionalidad | feat: implementar autenticación JWT |
| fix | Corrección de errores | fix: corregir validación login |
| refactor | Reestructuración de código | refactor: extraer mapper |
| docs | Documentación | docs: actualizar Arquitectura.md |
| test | Pruebas | test: agregar UsuarioServiceTest |
| chore | Mantenimiento | chore: actualizar dependencias |

Se recomienda utilizar mensajes cortos y descriptivos.

---

# 18. Pull Request

Antes de integrar cambios a la rama principal deberán cumplirse las siguientes reglas:

- Compilar correctamente el proyecto.
- Ejecutar las pruebas correspondientes.
- Revisar que no existan conflictos.
- Actualizar la documentación cuando sea necesario.
- Solicitar revisión antes de realizar el merge.

---

# 19. Buenas prácticas

Durante el desarrollo deberán seguirse las siguientes recomendaciones:

- No subir credenciales al repositorio.
- Mantener actualizado `.env.example`.
- Utilizar variables de entorno para información sensible.
- Documentar nuevas funcionalidades.
- Mantener el código limpio y legible.
- Evitar duplicación de lógica.
- Favorecer la reutilización de componentes.
- Mantener una única responsabilidad por clase.
- Escribir código pensando en su mantenimiento futuro.

---

# 20. Evolución del proyecto

Estas convenciones podrán actualizarse conforme el proyecto incorpore nuevos módulos o cambien las necesidades del equipo.

Toda modificación importante en la arquitectura deberá reflejarse también en la documentación técnica para mantener la consistencia entre el código y la documentación.
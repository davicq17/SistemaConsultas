# API REST

## 1. Introducción

El Sistema de Gestión de Consultas Académicas expone una API REST desarrollada con Spring Boot.

La API permite a diferentes clientes consumir los recursos del sistema de forma segura mediante autenticación basada en JWT y siguiendo los principios de una arquitectura REST.

La documentación detallada de cada endpoint se encuentra disponible mediante Swagger/OpenAPI, mientras que este documento describe las convenciones generales utilizadas por toda la API.

---

# 2. Arquitectura REST

La API sigue un modelo REST donde cada recurso es identificado mediante una URL y las operaciones se realizan utilizando los métodos HTTP estándar.

Los recursos se representan en formato JSON y las solicitudes se procesan de manera Stateless.

Principios aplicados:

- Arquitectura Cliente-Servidor.
- Stateless.
- Interfaz uniforme.
- Recursos identificados mediante URI.
- Intercambio de información en formato JSON.

---

# 3. Formato de solicitudes

Las solicitudes que envían información al servidor utilizan el encabezado:

```http
Content-Type: application/json
```

Cuando un endpoint requiere autenticación, el cliente deberá incluir el encabezado:

```http
Authorization: Bearer <JWT>
```

El cuerpo de la solicitud deberá representarse mediante objetos JSON definidos por los DTO correspondientes.

---

# 4. Formato de respuestas

Las respuestas exitosas devuelven representaciones JSON del recurso solicitado o un mensaje indicando que la operación fue realizada correctamente.

Ejemplo:

```json
{
    "id": 1,
    "nombre": "Ingeniería de Sistemas"
}
```

La estructura concreta dependerá del DTO utilizado por cada endpoint.

---

# 5. Manejo de errores

Todos los errores son gestionados de forma centralizada mediante `GlobalExceptionHandler`.

Esto garantiza respuestas consistentes independientemente del módulo que genere la excepción.

Formato general:

```json
{
    "timestamp": "2026-07-29T14:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "La solicitud contiene errores.",
    "errors": {
        "correo": "El correo es obligatorio."
    }
}
```

### Descripción de los campos

| Campo | Descripción |
|--------|-------------|
| timestamp | Fecha y hora del error |
| status | Código HTTP |
| error | Nombre del error HTTP |
| message | Descripción general |
| errors | Errores de validación (si existen) |

---

# 6. Autenticación

La autenticación se realiza utilizando JWT.

Flujo general:

1. El cliente envía sus credenciales al endpoint de login.
2. El servidor valida la información.
3. Se genera un JWT.
4. El cliente almacena el token.
5. Todas las solicitudes posteriores deberán enviar dicho token.

---

# 7. Organización de la API

Actualmente la API se encuentra organizada en los siguientes módulos:

```text
/auth

/usuarios

/programas-academicos

/modulos

/solicitudes (pendiente de integración)
```

Cada módulo agrupa los endpoints relacionados con un único recurso del sistema.

---

# 8. Métodos HTTP utilizados

| Método | Uso |
|---------|-----|
| GET | Consultar recursos |
| POST | Crear recursos |
| PUT | Actualizar recursos |
| DELETE | Eliminación lógica de recursos |

La API utiliza los métodos HTTP de acuerdo con las convenciones REST.

---

# 9. Códigos HTTP

Los principales códigos utilizados por la API son:

| Código | Significado |
|---------|-------------|
| 200 OK | Operación exitosa |
| 201 Created | Recurso creado correctamente |
| 204 No Content | Operación realizada sin contenido de respuesta (cuando aplique) |
| 400 Bad Request | Error de validación o solicitud incorrecta |
| 401 Unauthorized | Token inexistente o inválido |
| 403 Forbidden | Acceso denegado |
| 404 Not Found | Recurso no encontrado |
| 500 Internal Server Error | Error interno del servidor |

---

# 10. Validaciones

La validación de datos se implementa mediante Bean Validation.

Los DTO utilizan anotaciones como:

- @NotBlank
- @Email
- @Size
- @NotNull

Cuando alguna validación falla, la API responde utilizando el formato de errores definido por `GlobalExceptionHandler`.

---

# 11. Swagger / OpenAPI

La documentación interactiva de la API se encuentra disponible mediante Swagger.
Se puede acceder a ella luego de ejecutar la API por medio de la URL: `http://localhost:8080/swagger-ui/index.html`

Swagger permite:

- Consultar todos los endpoints.
- Visualizar DTOs.
- Ejecutar solicitudes.
- Autenticarse mediante JWT.
- Probar los endpoints protegidos.

Swagger constituye la fuente principal para consultar el detalle de cada operación disponible.

---

# 12. Convenciones REST

Durante el desarrollo del proyecto se adoptaron las siguientes convenciones:

- Utilizar sustantivos para representar recursos.
- Emplear nombres de rutas en plural.
- Utilizar los métodos HTTP adecuados para cada operación.
- Mantener consistencia en las respuestas.
- Utilizar DTOs para desacoplar la capa de presentación del modelo de dominio.
- Gestionar los errores de forma uniforme.

Estas convenciones buscan facilitar el mantenimiento y la evolución de la API.

---

# 13. Versionado

Actualmente la API corresponde a su primera versión.

Aunque no se implementa un mecanismo explícito de versionado, futuras versiones podrán incorporar una estrategia basada en prefijos de ruta, por ejemplo:

```text
/api/v2/usuarios
```

Esto permitirá introducir cambios incompatibles sin afectar a los consumidores existentes.

---

# 14. Evolución futura

La API podrá ampliarse con nuevas funcionalidades, entre ellas:

- Integración del módulo de Solicitudes de Consulta.
- Nuevos recursos y endpoints.
- Versionado formal de la API.
- Paginación y filtrado avanzado.
- Ordenamiento de resultados.
- Documentación automática ampliada mediante OpenAPI.

El diseño actual busca facilitar la incorporación de estas mejoras sin afectar la arquitectura existente.
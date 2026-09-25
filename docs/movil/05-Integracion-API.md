# Integración con la API (Cliente Móvil)

## 1. Introducción

Toda la comunicación con el backend pasa por `ApiService.kt` (interfaz Retrofit con funciones `suspend` que devuelven `Response<T>`). Este documento lista qué endpoints consume la app, con qué DTOs y contra qué entorno apunta, para detectar brechas frente a la API real (ver `docs/05-API.md` y Swagger).

---

# 2. Configuración del cliente HTTP

`RetrofitClient.kt` (singleton `object`):

- `Retrofit` con `GsonConverterFactory` y cliente OkHttp con interceptor JWT.
- `BASE_URL` con tres constantes según entorno; **actualmente apunta a `SERVER_WIFI`**:

| Constante | Valor | Cuándo usarla |
|-----------|-------|---------------|
| `SERVER_ADB` | `http://localhost:8080/` | Dispositivo físico por ADB |
| `SERVER_EMULATOR` | `http://10.0.2.2:8080/` | Emulador Android |
| `SERVER_WIFI` | `http://192.168.1.6:8080/` | Dispositivo físico en la misma Wi-Fi (actual) |

> Cambiar de entorno implica editar `BASE_URL` y recompilar. Mejora futura: `BuildConfig` con URL por `buildType`/sabor.

---

# 3. Endpoints consumidos

## Auth

| Método | Ruta | Función |
|--------|------|---------|
| POST | `auth/login` | `login` |
| POST | `auth/register` | `register` |

No se consumen `auth/forgot-password` ni `auth/reset-password`.

## Usuarios

| Método | Ruta | Función |
|--------|------|---------|
| GET | `usuarios` | `getUsers` |
| GET | `usuarios/{id}` | `getUser` |
| PUT | `usuarios/{id}` | `updateUser` |
| DELETE | `usuarios/{id}` | `deleteUser` |

## Solicitudes de consulta

| Método | Ruta | Función |
|--------|------|---------|
| GET | `solicitudes-consultas` | `getConsultations` |
| POST | `solicitudes-consultas` | `createConsultation` |
| PUT | `solicitudes-consultas/{id}` | `updateConsultation` |
| PATCH | `solicitudes-consultas/{id}/estado` | `changeConsultationStatus` |
| PATCH | `solicitudes-consultas/{id}/recurso-fisico` | `assignConsultationResource` |
| PATCH | `solicitudes-consultas/{id}/docente` | `reassignConsultationTeacher` |

No se consume `mis-solicitudes`: la app filtra por usuario en el `ViewModel` (`students`/`teachers` por `Role`).

## Comentarios

| Método | Ruta | Función |
|--------|------|---------|
| GET | `comentarios/solicitud/{solicitudId}` | `getCommentsByConsultation` |
| POST | `comentarios` | `createComment` |
| PUT | `comentarios/{id}` | `updateComment` |

Sin eliminación de comentarios (el backend tampoco la expone como física; la app no implementa borrado).

## Catálogos (programas, módulos, sedes, bloques, recursos físicos)

CRUD completo en cada uno (GET lista, POST crear, PUT actualizar, DELETE eliminación lógica):

`programas`, `modulos`, `sedes`, `bloques`, `recursos-fisicos`.

## No consumidos

- `/notificaciones` (la app no tiene pantalla de notificaciones).
- `/reportes` (además, solo existe en la rama `feat/reportes`).

---

# 4. DTOs

Organizados en `data/remote/dto/` por agregado (`auth`, `catalog`, `comment`, `consultation`, `user`). Usan `@SerializedName` para los nombres del backend (`correo`, `id_usuario`, `rol`, `nombre`). Las pantallas complejas agregan helpers de presentación en el mismo DTO (p. ej. `consultation`: `label()`, `nextStatesFor()`, `needsReason()`, `allowsComments()`, `toShortTime()`), manteniendo ese formato fuera de las `Screen`.

---

# 5. Manejo de errores de red

Patrón aplicado en los `ViewModel` (p. ej. `LoginViewModel`, `ConsultationsViewModel`):

1. `isLoading = true, error = null` antes de llamar.
2. `response.isSuccessful && body != null` → actualiza datos, `isLoading = false`.
3. Respuesta no exitosa → `isLoading = false` + mensaje funcional ("Correo o contraseña incorrectos", "No se pudieron cargar las solicitudes").
4. Excepción (sin conexión, timeout, JSON inválido) → `isLoading = false` + "No se pudo conectar al servidor".
5. 401/403 con token → el interceptor expira la sesión y `SessionWatcher` redirige al login.

---

# 6. Brechas conocidas con la API

| Brecha | Impacto | Acción sugerida |
|--------|---------|-----------------|
| No se consume `mis-solicitudes` | Filtrado en cliente, más tráfico | Usar el endpoint del backend |
| No se consume `/notificaciones` | Sin avisos en la app | Nueva feature `notifications` con el patrón Screen/ViewModel/UiState |
| No hay recuperación de contraseña | Usuario bloqueado si olvida clave | Consumir `forgot/reset-password` |
| `BASE_URL` hardcodeada a Wi-Fi | Hay que recompilar por entorno | `BuildConfig` por `buildType` |

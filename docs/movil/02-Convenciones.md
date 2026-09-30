# Convenciones del Cliente Móvil

## 1. Objetivo

Este documento define las convenciones aplicadas en `iubconsultas/`. Su objetivo es mantener un código consistente entre pantallas, permitiendo que cualquier integrante agregue un nuevo módulo (p. ej. notificaciones) replicando el patrón existente.

---

# 2. Organización por feature

Cada funcionalidad de `ui/screen/<feature>/` se compone de tres archivos:

```text
ui/screen/consultations/
├── ConsultationsScreen.kt    # UI declarativa
├── ConsultationsViewModel.kt # Estado + llamadas a repositorios
└── ConsultationsUiState.kt   # data class inmutable con el estado
```

Features existentes: `login`, `register`, `home`, `users`, `programs`, `modules`, `sedes`, `blocks`, `resources`, `consultations`.

---

# 3. Convenciones de nombres

## Screens

Sufijo `Screen`, un composable público por archivo:

```text
LoginScreen, RegisterScreen, AdminHomeScreen, TeacherHomeScreen,
StudentHomeScreen, UsersScreen, ProgramsScreen, ModulesScreen,
SedesScreen, BlocksScreen, ResourcesScreen, ConsultationsScreen
```

## ViewModels

Sufijo `ViewModel`, una clase por feature que extiende `androidx.lifecycle.ViewModel`:

```text
LoginViewModel, ConsultationsViewModel, BlocksViewModel, ...
```

## UiState

Sufijo `UiState`, `data class` inmutable con valores por defecto:

```text
LoginUiState, ConsultationsUiState, BlocksUiState, ...
```

Campos habituales: `isLoading: Boolean = false`, `error: String? = null` más los datos de la pantalla.

## Repositorios

Sufijo `Repository` en `data/repository`, un agregado por clase:

```text
AuthRepository, UserRepository, ConsultationRepository, CommentRepository,
ProgramRepository, ModuleRepository, SedeRepository, BlockRepository, ResourceRepository
```

## DTOs

`data class` en `data/remote/dto/<agregado>/`, con `@SerializedName` cuando el JSON del backend usa otro nombre (p. ej. `correo` → `email`, `id_usuario` → `id`, `rol` → `role`):

```text
LoginRequest, LoginResponse, RegisterRequest, Role,
UserResponse, UpdateUserRequest,
ConsultationResponse, CreateConsultationRequest, UpdateConsultationRequest,
ChangeStatusRequest, AssignResourceRequest, ReassignTeacherRequest,
CommentResponse, CreateCommentRequest, UpdateCommentRequest,
ProgramResponse, CreateProgramRequest, UpdateProgramRequest, ...
```

---

# 4. Reglas de código

- Una clase/composable público por archivo.
- La `Screen` solo lee `viewModel.uiState` y llama a funciones del `ViewModel`; nunca importa `RetrofitClient` ni repositorios.
- El `ViewModel` expone `var uiState by mutableStateOf(...)` con setter privado y actualiza por `copy()`.
- Toda llamada de red va en `viewModelScope.launch` con `try/catch`: `catch (e: Exception)` → mensaje de conexión en `error`.
- Los repositorios reciben `ApiService` por constructor y no conocen Compose.
- Mensajes de error visibles en español, claros y sin tecnicismos (p. ej. "No se pudo conectar al servidor").

---

# 5. Componentes reutilizables

Todo input/botón/diálogo de uso común vive en `ui/component` y se reutiliza en lugar de duplicarse:

`AppHeader`, `PrimaryButton`, `InputField`, `EmailField`, `PasswordField`, `ClickableText`, `DropDownSelector`, `SelectDialog`, `EditDialog`, `RoleSelector`.

---

# 6. Navegación

Los destinos se declaran como `object` en el `sealed class AppDestination` (`Login`, `Register`, `AdminHome`, `TeacherHome`, `StudentHome`) y se registran una sola vez en `AppNavHost`. No crear rutas literales fuera de `AppDestination`.

---

# 7. Estilo y tema

Colores, tipografías y tema Material3 centralizados en `ui/theme` (`Color.kt`, `Type.kt`, `Theme.kt`). Las pantallas usan `IubconsultasTheme` desde `MainActivity`; no hardcodear colores en las `Screen`.

# Convenciones del Cliente Web (Angular)

## 1. Objetivo

Este documento define las convenciones aplicadas en `FRONTEND/`. Su objetivo es que cualquier integrante agregue un módulo (p. ej. `notificaciones`) replicando el patrón existente, con formularios y tipos 1:1 con el backend. Complementa a `03-Arquitectura.md` (el *qué*) con el *cómo*.

Referencias de contrato: `docs/05-API.md`, Swagger (`http://localhost:8080/swagger-ui/index.html`) y `docs/web/02-Auditoria-Cliente-Web-Backend.md` (cada regla cita el hallazgo que evita).

---

# 2. Organización por feature

Cada feature de `app/features/<recurso>/` se compone de:

```text
features/solicitudes/
├── solicitudes.routes.ts       # ROUTES + guards de la feature
├── services/
│   └── solicitudes.service.ts  # HttpClient tipado, un método por endpoint
├── pages/                      # componentes con routing (uno por vista)
│   ├── solicitudes-list.page.ts
│   ├── solicitud-create.page.ts
│   └── mis-solicitudes.page.ts
└── components/                 # componentes tontos solo de esta feature
    └── cambio-estado-dialog.component.ts
```

Si un componente/servicio se usa en dos o más features, se promueve a `shared/` o `core/`; si es de una sola feature, se queda en ella.

---

# 3. Convenciones de nombres

- Archivos en `kebab-case` con sufijo de rol: `solicitudes.service.ts`, `auth.guard.ts`, `jwt.interceptor.ts`, `solicitudes.routes.ts`, `solicitudes-list.page.ts`, `cambio-estado-dialog.component.ts`.
- Clases en `PascalCase`: `SolicitudesService`, `SolicitudesListPage`, `CambioEstadoDialogComponent`. Funciones guard/interceptor en `camelCase`: `authGuard`, `roleGuard`, `jwtInterceptor`, `errorInterceptor`.
- Interfaces de modelo = nombre del DTO sin prefijo `I` y sin sufijo salvo el de uso: `LoginRequest`, `SolicitudResponse`, `RegistroSolicitudRequest`, `CambiarEstadoRequest`. Enums en `PascalCase` con valores del backend: `EstadoSolicitud.PENDIENTE`, `PrioridadSolicitud.ALTA`, `Rol.ESTUDIANTE`.
- Rutas URL en minúsculas y plural del recurso (`/solicitudes`, `/recursos-fisicos`, `/mis-solicitudes` como subruta de solicitudes). Sin literales de ruta fuera de `*.routes.ts` (evita el W-01 de `docente.html` vs `Docente.html`).

---

# 4. Reglas de código

- Un componente/servicio público por archivo; componentes `standalone: true`, `ChangeDetectionStrategy.OnPush` por defecto.
- Estado con `signal`s (`isLoading`, `error`, datos); la plantilla solo lee signals y emite eventos. Prohibido guardar estado de negocio en el componente fuera de signals.
- Toda llamada HTTP va en el `*.service` de la feature con tipos de `models/` y operadores RxJS (`map/catchError`); el componente se suscribe (o usa `toSignal`) y traduce el error a mensaje visible. El componente nunca importa `HttpClient` ni construye URLs.
- `strict` de TypeScript activado: prohibidos `any` y los fallbacks `x || y || z` para "adivinar" el contrato (W-16). Si un campo puede venir ausente, se modela opcional (`?`) y se decide un visible explícito (`'Sin programa'`, `'Sin asignar'`), no un encadenado silencioso.
- Payloads exactos 1:1 con el DTO (W-04/W-06/W-09): crear solicitud envía solo `prioridad, asunto, descripcion, fechaConsulta, horaConsulta, docenteId, moduloId`; editar usuario envía solo `nombre, apellido, correo, programaId`; crear programa/sede envía solo `nombre`; módulo envía `nombre + descripcion`. Nada de `numeroConsulta`, `estudianteId`, `recursoFisicoId`, `id/identificacion/email/rol/activo/estado`, `codigo`, `ubicacion`.
- Sin valores inventados de enums (W-02/W-10): Aceptar → `EN_PROCESO`; Rechazar → `RECHAZADA` + `motivo`; prioridades solo `ALTA/MEDIA/BAJA`; calendario con los 5 estados reales.
- Sin UI sin backend ni backend sin UI silencioso (W-12/W-13): quitar columna firma/QR y `html5-qrcode`; comentarios siempre vía `/comentarios/solicitud/{id}`; mostrar `nombrePrograma`; eliminar el modal muerto de edición y la ruta a `formato.html`, o implementarlos de verdad.
- Mensajes de error visibles en español, sin tecnicismos (p. ej. "No se pudo conectar al servidor", "Correo o contraseña incorrectos").

---

# 5. Formularios reactivos

- Siempre `ReactiveFormsModule` + `FormBuilder`; validación espejo de Bean Validation con las longitudes del DTO (ej. nombre programa 4–100, módulo nombre 3–100 + descripción 10–500 con contador visible, sede nombre 3–100).
- Reglas que evitan W-03/W-08/W-09/W-11:
  - Rechazar/cancelar exige `motivo` (diálogo con campo obligatorio, no `prompt`).
  - Registro de `ESTUDIANTE` exige `programaId` (el backend lo deja opcional; la web lo pide siempre).
  - Sin campos fantasma obligatorios ("código", "ID", "ubicación"); el recurso físico al crear es opcional con aviso "el lugar se asigna después" (W-11).
  - El rol docente no tiene formulario de creación de solicitudes (W-05).
- No rechazar en cliente lo que el servidor aceptaría: si la auditoría detectó discrepancias de patrón/longitud (W-08, nombre/password), el mensaje del validador debe citar el límite real del backend.

---

# 6. Routing, guards e interceptores

- Rutas: cada feature expone `*_ROUTES: Routes`; `app.routes.ts` solo hace `loadChildren` + `canMatch: [authGuard, roleGuard]` con `data: { roles: [...] }`. Redirección por defecto según rol (admin/docente/estudiante), más `/auth/**` público y wildcard a login.
- Guards: `authGuard` (sin token → `/auth/login` con `returnUrl`); `roleGuard` (rol no permitido → home de su rol, no 403 en blanco).
- `jwtInterceptor`: añade `Authorization: Bearer` a todo salvo `/auth/**`; lee el token de `session.service`, nunca de `localStorage` directamente desde componentes.
- `errorInterceptor`: ante 401/403 limpia la sesión y redirige a login; ante 400 de validación propaga `errors` por campo para pintarlos bajo cada input; ante `HttpMessageNotReadable` (enum/campo inválido, W-15) muestra el mensaje del backend cuando el handler exista, con fallback genérico.

---

# 7. Estilo y formato

- Tailwind para layout + componentes propios en `shared/components`; nada de colores hardcodeados en páginas (cuando haya tema, centralizarlo como hace `ui/theme` en el móvil).
- Prettier (`.prettierrc`) como formato único; `pnpm build`/`test` deben pasar antes de cada PR (misma exigencia que el backend: compilar + probar en Swagger + actualizar docs).
- `styles.css` solo para base global; estilos de feature junto a su componente.

---

# 8. Lista de "no hacer" (resumen auditoría)

1. No enviar campos que el DTO no define (W-04, W-06, W-09).
2. No inventar estados/prioridades (W-02, W-10).
3. No pedir `motivo` después: es obligatorio para rechazar/cancelar (W-03).
4. No crear solicitudes como docente (W-05).
5. No inactivar usuarios con `PUT` + campos extra ni con `PATCH` inexistente: usar `DELETE` (lógica) y esperar endpoint de restaurar (W-06).
6. No reenviar `programaId: null` si no cambió (W-07).
7. No dejar el programa fuera del registro de estudiantes (W-08).
8. No prometer recurso/firma/comentario que la API no devuelve (W-11, W-12).
9. No filtrar `mis-solicitudes` en cliente cuando el backend exponga la ruta (W-13).
10. No depender de rutas públicas accidentales: verificar W-14 tras cerrar el `POST` anónimo.

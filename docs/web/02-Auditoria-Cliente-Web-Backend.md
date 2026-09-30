# Auditoría de inconsistencias: Cliente Web vs Backend/BD

> Fecha: 2026-09-25. Rama auditada: `develop`. Alcance: `Frontend/` (HTML/CSS/JS vanilla) contra `src/main/kotlin` (DTOs, entidades, controllers, services, mappers) y el modelo JPA.
> Objetivo: dejar constancia verificada de cada inconsistencia para corregirla durante la migración a Angular. Cada hallazgo indica severidad, ubicación exacta, evidencia del contrato y el camino de corrección recomendado.
> Convención de severidad: 🔴 bloqueante (la función no puede operar) · 🟠 pérdida/corrupción de datos o seguridad · 🟡 UX/funcionalidad degradada · ⚪ menor/cobertura.

---

# Resumen ejecutivo

De 16 hallazgos, 7 son bloqueantes: los botones Aceptar/Rechazar del docente (W-02, W-03), la creación de solicitudes (W-04, W-05), la edición y activación de usuarios (W-06) y dos redirecciones rotas del login (W-01). El patrón raíz se repite: **el cliente envía campos que no existen en los DTOs** (`numeroConsulta`, `estudianteId`, `recursoFisicoId`, `ubicacion`, `email`, `rol`, `activo`, `estado`, `id`) y **usa valores de enumeraciones que el backend no define** (`ACEPTADA`, `URGENTE`, `AGENDADA`, `REALIZADA`). Además hay campos de formulario obligatorios que el JS ignora (el caso del "código" del amigo: la recomendación es **quitarlos del cliente, no agregar columnas a la BD**). Todo el detalle, abajo.

---

# 🔴 W-01 — Redirecciones del login a archivos que no existen

## Descripción

Tras un login exitoso con rol DOCENTE o ESTUDIANTE, el cliente redirige a `docente.html` y `estudiante.html` en minúsculas. Esos archivos no existen: los reales son `Docente.html` y `Estudiante.html` (con mayúscula). En Windows (desarrollo local) funciona por sistema de archivos insensible a mayúsculas; en Linux/Docker/Nginx devuelve 404 y el usuario queda bloqueado tras autenticarse.

## Ubicación

- `Frontend/assets/javascript/index.js:51` → `window.location.href = 'docente.html'`
- `Frontend/assets/javascript/index.js:54` → `window.location.href = 'estudiante.html'`
- Archivos reales: `Frontend/Docente.html`, `Frontend/Estudiante.html` (la rama de `ADMINISTRADOR` a `Administrador.html:49` sí es correcta).

## Evidencia del contrato

No aplica (es interno del cliente), pero agrava W-14: el servidor Dockerizado servirá en Linux.

## Recomendación

En Angular el problema desaparece solo (routing por rutas, no por archivos). Como corrección inmediata y barata: cambiar a `'Docente.html'` y `'Estudiante.html'`. No requiere ningún cambio de backend/BD.

---

# 🔴 W-02 — El botón "Aceptar" envía un estado inexistente (`ACEPTADA`)

## Descripción

El botón "✅ Aceptar" de la tabla del docente envía `PATCH /solicitudes-consultas/{id}/estado` con `{ estado: "ACEPTADA" }`. Ese valor no existe en la enumeración del backend, por lo que la deserialización del enum falla y la API responde 400 siempre: **ninguna solicitud puede aceptarse desde el cliente web**.

## Ubicación

- `Frontend/assets/javascript/Docentes.js:274` → `responderSolicitud(s.id, "ACEPTADA")`
- `Frontend/assets/javascript/Docentes.js:289-294` → envía `{ estado }` sin `motivo`.

## Evidencia del contrato

- `src/.../model/enums/EstadoSolicitud.kt` → `PENDIENTE, RECHAZADA, EN_PROCESO, RESUELTA, CANCELADA` (no existe `ACEPTADA`).
- `src/.../dto/solicitud/CambiarEstadoSolicitudRequest.kt` → `estado: EstadoSolicitud` (deserialización estricta del enum).
- Transiciones válidas (`SolicitudConsultaServiceImpl.kt:264-296`): `PENDIENTE → EN_PROCESO | RECHAZADA | CANCELADA`; `EN_PROCESO → RESUELTA | CANCELADA`.

## Recomendación

Mapear la intención del negocio a estados reales: **Aceptar → `EN_PROCESO`**, Rechazar → `RECHAZADA` (con motivo, ver W-03). Es decisión de negocio a confirmar con el equipo, pero no requiere cambios de backend/BD: solo corregir el cliente (Angular: `responderSolicitud` con enum tipado del contrato). Los tests de aceptación deben cubrir `PENDIENTE→EN_PROCESO` y `PENDIENTE→RECHAZADA`.

---

# 🔴 W-03 — Rechazar falla siempre: falta el `motivo` obligatorio

## Descripción

El botón "❌ Rechazar" envía `{ estado: "RECHAZADA" }` sin `motivo`. El backend exige motivo no vacío para `RECHAZADA` y `CANCELADA`, así que responde 400 siempre.

## Ubicación

- `Frontend/assets/javascript/Docentes.js:276-279` y `289-294` (mismo `responderSolicitud`, sin campo de motivo en `Docente.html`).

## Evidencia del contrato

- `SolicitudConsultaServiceImpl.kt:253-262` (`validarMotivo`): `RECHAZADA`/`CANCELADA` sin motivo → `BadRequestException("Debe indicar un motivo...")`.
- `CambiarEstadoSolicitudRequest.kt` → `motivo: String?` (el campo existe, el cliente no lo usa).

## Recomendación

Pedir el motivo en la UI antes de confirmar (modal o `prompt`, mejor modal con validación) y enviarlo en el PATCH. Sin cambios de backend/BD. En Angular: el diálogo de rechazo con campo obligatorio es criterio de aceptación.

---

# 🔴 W-04 — Crear solicitud envía campos que el DTO no define

## Descripción

Ambos formularios de creación envían `estudianteId`, `recursoFisicoId` y `numeroConsulta`, ninguno existe en `RegistroSolicitudConsultaRequest`. Con la deserialización estricta de Spring Boot (comportamiento por defecto, sin `fail-on-unknown-properties` desactivado en `application.yaml`), la API responde 400 `Unrecognized field`: **la creación falla**. Y aunque el modo fuera permisivo, esos valores se ignorarían en silencio (el número lo genera el servidor, el estudiante sale del JWT y el recurso queda `null`), es decir, la UI promete lo que no ocurre.

## Ubicación

- `Frontend/assets/javascript/Docentes.js:124-135` (`estudianteId`, `recursoFisicoId`, `numeroConsulta: "C-" + Date.now()`).
- `Frontend/assets/javascript/Estudiante.js:305-316` (`estudianteId`, `recursoFisicoId`, `numeroConsulta: "SC-" + Date.now()`).

## Evidencia del contrato

- `src/.../dto/solicitud/RegistroSolicitudConsultaRequest.kt` → solo `prioridad, asunto, descripcion, fechaConsulta, horaConsulta, docenteId, moduloId`.
- `SolicitudConsultaServiceImpl.kt:38-71` → `numeroConsulta = NumeroConsultaGenerator.generar(...)` (formato `SC-AAAA-NNNNNN`, ej. `SC-2026-000002`, ver `util/NumeroConsultaGenerator.kt`); `estudiante = obtenerEstudiante()` (del JWT, nunca del body); `recursoFisico = null` (se asigna después vía `PATCH .../recurso-fisico`).

## Recomendación

Enviar el payload exacto del contrato (7 campos) y eliminar la generación client-side del número. En Angular: interfaz `RegistroSolicitudRequest` tipada 1:1 con el DTO y test que rechace en compilación cualquier campo extra. Sin cambios de backend/BD. (El `C-`/`SC-` con timestamp además colisiona conceptualmente con el formato oficial `SC-AAAA-NNNNNN`: quitarlo evita confusión en reportes.)

---

# 🔴 W-05 — El docente no puede crear solicitudes por diseño del backend

## Descripción

El portal docente incluye "Registrar Consulta", pero `registrar()` toma el estudiante del usuario autenticado y exige rol `ESTUDIANTE`. Un docente autenticado recibe siempre 400 (`"El usuario indicado no tiene el rol ESTUDIANTE."`). El formulario es inoperable por diseño, no por un typo.

## Ubicación

- `Frontend/Docente.html:94-134` (formulario `consultaForm`), `Docentes.js:117-158`.

## Evidencia del contrato

- `SolicitudConsultaServiceImpl.kt:45,202-208` (`obtenerEstudiante()` exige `rol == ESTUDIANTE`).

## Recomendación

**Quitar el formulario de creación del rol docente en Angular** (el docente gestiona: acepta, rechaza, comenta, reasigna; no crea). Es el camino más beneficioso: cero cambios de backend/BD y coherencia con el dominio (la solicitud nace del estudiante). Solo si el negocio exige "crear por encargo" se justificaría un endpoint específico de creación por terceros — hoy no existe y no se recomienda improvisarlo en la migración.

---

# 🔴 W-06 — Editar y activar/inactivar usuarios: payload incompatible y efectos nulos

## Descripción

Tres fallos encadenados en la gestión de usuarios del administrador:

1. `guardarEdicionUsuario` y `cambiarEstadoUsuario` envían `PUT /usuarios/{id}` con campos inexistentes en `UpdateUsuarioRequest` (`id`, `identificacion`, `email`, `rol`, `activo`, `estado`) → 400 en modo estricto.
2. Aunque la petición pasara, **el cambio de rol y de activo se ignoran**: el servicio solo actualiza `nombre, apellido, correo, programa`.
3. El fallback `PATCH /usuarios/{id}/estado` (ante un 405) apunta a un endpoint que **no existe** en `UsuarioController` (solo hay GET/PUT/DELETE): código muerto que nunca puede funcionar. Y la vía real de inactivación, `DELETE /usuarios/{id}` (eliminación lógica, implementada), jamás se usa; para **reactivar** no existe endpoint alguno.

Resultado: ni editar (con esos extras), ni cambiar rol, ni inactivar, ni reactivar operan correctamente desde el cliente.

## Ubicación

- `Frontend/assets/javascript/Administrador.js:254-297` (`guardarEdicionUsuario`, payload líneas 267-277).
- `Frontend/assets/javascript/Administrador.js:299-348` (`cambiarEstadoUsuario`, payload líneas 311-322, fallback líneas 330-335).

## Evidencia del contrato

- `src/.../dto/usuario/UpdateUsuarioRequest.kt` → solo `nombre, apellido, correo, programaId`.
- `src/.../service/impl/UsuarioServiceImpl.kt:34-67` → solo asigna esos 4 campos; `eliminarUsuario` (líneas 69-76) hace `activo = false` vía DELETE.
- `src/.../controller/UsuarioController.kt` → rutas GET, GET `/{id}`, PUT `/{id}`, DELETE `/{id}` (no hay PATCH `/{id}/estado`).

## Recomendación

- Editar: enviar exactamente `{ nombre, apellido, correo, programaId }`. El cambio de **rol** no existe en el contrato: decidir en el equipo si se habilita (nuevo campo `rol` en el DTO de actualización, restringido a ADMIN) o se retira el selector de rol del modal. No asumirlo en silencio.
- Inactivar: usar `DELETE /usuarios/{id}` (ya implementado).
- Reactivar: **agregar endpoint en el backend** (p. ej. `POST /usuarios/{id}/restaurar`), es el único cambio backend necesario de este hallazgo y es pequeño.
- Eliminar el fallback al PATCH inexistente.
- Ver además W-07 (pérdida de programa) antes de tocar este flujo.

---

# 🟠 W-07 — Inactivar/editar puede borrar el programa del estudiante

## Descripción

`cambiarEstadoUsuario` envía `programaId: usuario.programaId || usuario.programa?.id || null`. Pero `UsuarioResponse` no trae `programaId` plano accesible con ese nombre en el objeto que guarda la tabla (el mapper sí lo incluye como `programaId`, mas el flujo de edición trabaja con `programa?.id`), y cuando vale `null` el servicio ejecuta `this.programa = programa` con `null`: **un estudiante pierde su programa académico como efecto colateral de inactivarlo**. Lo mismo aplica a `guardarEdicionUsuario` (línea 275) cuando el programa no se resolvió.

## Ubicación

- `Frontend/assets/javascript/Administrador.js:265,275,319` y `UsuarioServiceImpl.kt:57-61`.

## Evidencia del contrato

- `src/.../mapper/UsuarioMapper.kt:22-23` → la respuesta sí trae `programaId` y `programaNombre`; el cliente no los conserva en `todosLosUsuarios` de forma utilizable y reenvía `null`.

## Recomendación

En Angular: el modal de edición debe precargar el `programaId` real (del GET) y **no enviar el campo si no cambió**; mejor aún, que el backend trate `programaId: null` como "sin cambio" en lugar de "desasignar" (ajuste mínimo en `actualizarUsuario`, a acordar). Prioritario porque es corrupción silenciosa de datos, no solo un 400 visible.

---

# 🟡 W-08 — Registro público: el programa se carga pero nunca se envía

## Descripción

`registro.html` tiene select de programa y `cargarProgramas()` lo llena (ese GET es público, ver W-14), pero el payload de registro no incluye `programaId` (línea comentada) → **todo estudiante auto-registrado queda "Sin programa"**, y así lo mostrará después cada solicitud (`nombrePrograma = "Sin programa"` en el mapper). Control decorativo que confunde.

## Ubicación

- `Frontend/assets/javascript/registro.js:12` (lectura comentada), `:43-50` (payload sin `programaId`), `:79-103` (`cargarProgramas`).

## Evidencia del contrato

- `AuthServiceImpl.kt:76-82` → `programaId` opcional pero soportado (resuelve el programa si llega).
- `mapper/SolicitudConsulta.kt:37` → `"Sin programa"` cuando el estudiante no tiene.

## Recomendación

Incluir `programaId` en el registro y **exigirlo para ESTUDIANTE en Angular** (el backend lo deja opcional; endurecerlo en backend es opcional y rompería compatibilidad con la app móvil: no recomendado ahora). Alineación menor adicional: el frontend valida nombre solo-letras 3-50 y password con especial, mientras el backend acepta hasta 100 caracteres sin patrón y password de mínimo 8 sin especial → unificar mensajes para no rechazar en cliente lo que el servidor aceptaría (o viceversa).

---

# 🟡 W-09 — Formularios admin con campos obligatorios fantasma (caso "código" y "ubicación")

## Descripción

Tres formularios exigen campos que el backend y la BD no conocen (y el propio JS termina ignorando o enviando como desconocidos):

| Formulario | Campo fantasma obligatorio | Realidad backend/BD |
|---|---|---|
| Registrar Programa (`Administrador.html:179-180`, `idPrograma`) | "ID del programa" | El `id` lo autogenera la BD (`GenerationType.IDENTITY`); `registrarPrograma()` ni lo lee (solo `nombre`) |
| Registrar Módulo (`Administrador.html:193-194`, `idModulo` "Código") | "Código" obligatorio | `Modulo` no tiene código; además **falta el campo `descripcion`**, que el backend exige (`@NotBlank`, 10-500). El JS lee `descripcionModulo` (inexistente) y usa el nombre como fallback → 400 si el nombre tiene menos de 10 caracteres |
| Registrar Sede (`Administrador.html:221-228`, `idSede` + `ubicacionSede`) | "ID" y "Ubicación / Dirección" obligatorios | `Sede` solo tiene `nombre`; `registrarSede()` envía `id` y `ubicacion` como propiedades desconocidas (`Administrador.js:505-517`) |

Es exactamente el caso que mencionabas del "código": existe en la pantalla, no existe en backend/BD.

## Ubicación

- HTML: `Frontend/Administrador.html:176-232`. JS: `Administrador.js:422-437` (programa), `:457-473` (módulo), `:504-537` (sede).
- Contrato: `RegistroProgAcademicoRequest.kt` (solo `nombre`, 4-100), `RegistroModuloRequest.kt` (`nombre` 3-100 + `descripcion` 10-500), `RegistroSedeRequest.kt` (solo `nombre`, 3-100); entidades `ProgramaAcademico`, `Modulo`, `Sede` sin esos atributos.

## Recomendación

**Quitar los campos del cliente; no agregar columnas a la BD.** Agregar `codigo`/`ubicacion` implicaría nuevas columnas, unicidades y validaciones que ningún requerimiento pide, y arrastraría al móvil y reportes. En Angular: formularios 1:1 con cada DTO (programa: solo nombre; módulo: nombre + **descripción visible con contador 10-500**; sede: solo nombre; bloque: nombre + sede; recurso: nombre + tipo + bloque) con las longitudes del backend como validación espejo. Solo si el negocio necesitas códigos/ubicaciones reales, modelarlos entonces como requerimiento formal (migración Flyway/Liquibase + DTOs + móvil), nunca como parche de migración.

---

# 🟡 W-10 — Opción `URGENTE` y estados de calendario inexistentes

## Descripción

El select de prioridad del docente ofrece `URGENTE` (`Docente.html:123`) y el calendario del estudiante colorea por `ACEPTADA/AGENDADA/REALIZADA` (`Estudiante.js:182-195`). Ninguno existe en `PrioridadSolicitud` (`ALTA, MEDIA, BAJA`) ni en `EstadoSolicitud`: seleccionar Urgente → 400; los eventos del calendario nunca matchean esos estados.

## Ubicación

- `Frontend/Docente.html:117-124`, `Frontend/assets/javascript/Estudiante.js:157-234`.

## Recomendación

Quitar `URGENTE` y reescribir la leyenda del calendario con los 5 estados reales (ej. PENDIENTE azul, EN_PROCESO rojo, RESUELTA verde, RECHAZADA/CANCELADA gris). Sin cambios de backend/BD.

---

# 🟡 W-11 — El "Lugar" obligatorio al solicitar se descarta en silencio

## Descripción

El estudiante debe elegir recurso físico (`Estudiante.html:71 required`), pero `registrar()` fija `recursoFisico = null` y el campo enviado se ignora (W-04): la tabla muestra luego "Sin asignar". El usuario cree reservar un lugar que nadie registró.

## Ubicación

- `Frontend/Estudiante.html:70-71`, `Estudiante.js:313`, `SolicitudConsultaServiceImpl.kt:62`.

## Recomendación

Hacerlo **opcional con aviso explícito** ("el lugar se asigna después") en Angular, que es coherente con el flujo existente (`PATCH .../recurso-fisico` por docente/admin, con validación de estado `PENDIENTE|EN_PROCESO`). Extender el backend para aceptar `recursoFisicoId` opcional al crear es posible pero cambia reglas y notificaciones: no recomendado en esta fase.

---

# 🟡 W-12 — Columnas con datos que la API nunca devuelve (`firma`, `comentario`, `programa`)

## Descripción

- Admin muestra "Firma / QR" (`Administrador.js:121-134`): `SolicitudConsultaResponse` no tiene `firma`; siempre cae en "❌ No Firmado". Además `Docente.html:183` carga `html5-qrcode` sin ningún uso.
- Estudiante muestra "Comentario" (`Estudiante.js:419` con `comentarioDocente || observacion`): tampoco existen en la respuesta; siempre "—". Los comentarios reales viven en `/comentarios` (ver W-13).
- Estudiante muestra `s.programa` (`Estudiante.js:412`): el campo real es `nombrePrograma`.

## Ubicación

- `Administrador.js:109-136`, `Estudiante.js:409-421`, `SolicitudConsultaResponse.kt` (41 líneas: sin `firma`, sin comentarios, con `nombrePrograma`).

## Recomendación

Quitar la columna de firma/QR y la librería sin uso (la firma digital, si algún día es requerimiento, es proyecto propio: entidad, consentimiento y validador). Mostrar comentarios consultando `/comentarios/solicitud/{id}` (ya existe en backend) y usar `nombrePrograma`. Sin cambios de backend salvo que se pida firma real.

---

# ⚪ W-13 — Funcionalidad backend sin UI (brecha de cobertura para Angular)

## Descripción

Existe y funciona en la API, pero el cliente web jamás lo usa (cero llamadas en los 5 JS): **comentarios** (`RegistroComentarioRequest: contenido + solicitudConsultaId`), **notificaciones**, **recuperación de contraseña**, **actualizar solicitud** (`PUT .../{id}`, con `motivo` obligatorio si cambia fecha/hora), **asignar recurso**, **reasignar docente**. A la inversa, hay UI sin backend: modal "Editar Consulta" (`Docente.html:150-174`) sin ningún listener en `Docentes.js`, y `exportarformato()` redirige a `formato.html`, que **no existe** en `Frontend/`.

## Ubicación

- Búsqueda sin resultados en `Frontend/assets/javascript/` para: `/comentarios`, `/notificaciones`, `forgot`, `reset-password`, `mis-solicitudes`, `formEditarConsulta`, `cerrarModalEdicion`.
- `Docentes.js:393-395` (`formato.html` inexistente); `Administrador.js:598-607` (`obtenerRecursos` solo hace `console.log`, sin tabla); sedes/bloques solo alimentan selects, sin listados.

## Evidencia y nota de backend

`ComentarioController`, `NotificacionController`, `UpdateSolicitudConsultaRequest` (con `motivo`), endpoints PATCH de recurso/docente existen y están documentados en Swagger. Ojo: `listarMisSolicitudes()` está implementado en el **service** pero **sin ruta en el controller** (`SolicitudConsultaController.kt` no la expone; confirmado por búsqueda: solo vive en `SolicitudConsultaService.kt:18` y su impl): hoy web y móvil filtran en cliente descargando todo.

## Recomendación

Backlog priorizado para Angular: (1) comentarios por solicitud, (2) notificaciones, (3) gestión de estado/recurso/docente para admin-docente, (4) recuperación de contraseña, (5) edición de solicitud con motivo. Y en backend, **exponer `GET /solicitudes-consultas/mis-solicitudes` antes que `/.../{id}`** (orden de rutas) para que Angular deje de filtrar en cliente —esto también corrige los filtros frágiles actuales (docente filtra por `identificacionDocente`, estudiante mezcla `estudianteId`/`identificacionEstudiante`). Eliminar el modal muerto y `formato.html`, o implementarlos de verdad.

---

# 🟠 W-14 — Rutas sensibles abiertas sin autenticación (el web depende de ello)

## Descripción

`SecurityConfig` deja `permitAll` a la ruta exacta `/solicitudes-consultas` (todos los métodos sobre esa ruta: **listar y crear sin JWT**) y a `GET /programas`. El cliente funciona "gracias" a ello (registro carga programas sin token; listados sin sesión), pero **cualquiera puede crear solicitudes sin autenticarse**. No es un bug del JS, pero la migración debe resolverlo conscientemente.

## Ubicación

- `src/.../config/SecurityConfig.kt:35-51`.

## Recomendación

Mantener público solo lo imprescindible y documentado (`GET /programas` para el registro; idealmente con justificación en `docs/04-Seguridad.md`), y **exigir JWT en `POST /solicitudes-consultas`** (Angular ya adjunta el token en todo). Verificar tras el cambio que el registro siga cargando programas y que ningún flujo anónimo legítimo se rompa. Clasificado seguridad: atender antes del despliegue dockerizado.

---

# 🟡 W-15 — Errores de deserialización fuera del formato uniforme

## Descripción

`GlobalExceptionHandler` cubre `ResourceNotFound`, `BadRequest` y validaciones Bean, pero **no `HttpMessageNotReadableException`** (enums inválidos como `ACEPTADA`/`URGENTE`, fechas con formato erróneo, propiedades desconocidas). Esos 400 salen con el formato por defecto de Spring, no con el `ErrorResponse {timestamp,status,error,message,errors}` que el cliente espera (`data.message` queda indefinido y el usuario ve "Error al registrar la solicitud" sin detalle).

## Ubicación

- `src/.../exception/GlobalExceptionHandler.kt` (sin handler para ese caso).

## Recomendación

Agregar el handler que devuelva el mismo `ErrorResponse` con mensaje accionable (ej. "estado 'ACEPTADA' no válido; valores: PENDIENTE, EN_PROCESO..."). Cambio backend pequeño, alto valor para Angular (que podrá mostrar errores de campo reales) y para la app móvil.

---

# ⚪ W-16 — Fallbacks defensivos que enmascaran el contrato

## Descripción

El JS está lleno de `x || y || z` (`p.id || p.codigo`, `u.correo || u.email`, `data.content || data.modulos`, `u.nombrePrograma || u.programa?.nombre...`) y `option.value = d.id || d.idUsuario` con `opt.textContent = d.nombre` (sin apellido). Funcionan por tolerancia, pero **ocultan desalineaciones** (ej. `nombrePrograma` real vs `nombrePrograma` inventado en admin, que sí existe; o `d.nombre` sin apellido recortando identidad) y dificultan detectar rupturas del contrato.

## Ubicación

- Ejemplos: `Docentes.js:105-106`, `Estudiante.js:110-111,133-143`, `Administrador.js:183,189,416-417,661-662`.

## Recomendación

En Angular: **interfaces TypeScript exactas por DTO** (generadas o copiadas de Swagger) y cero fallbacks salvo migraciones documentadas; mostrar `nombre + apellido` y `programaNombre` reales. Los fallbacks actuales sirven como mapa de sospechas al escribir los tipos, no como patrón a heredar.

---

# Tabla resumen para el plan Angular

| ID | Severidad | Corrección | Toca backend/BD |
|----|-----------|------------|-----------------|
| W-01 | 🔴 | Corregir rutas (Angular routing) | No |
| W-02 | 🔴 | Aceptar → `EN_PROCESO` | No (confirmar negocio) |
| W-03 | 🔴 | Pedir `motivo` al rechazar | No |
| W-04 | 🔴 | Payload exacto de 7 campos | No |
| W-05 | 🔴 | Quitar "Registrar" del docente | No |
| W-06 | 🔴 | Payload exacto + DELETE para inactivar + nuevo endpoint restaurar + quitar fallback | Sí (1 endpoint pequeño) |
| W-07 | 🟠 | No enviar `programaId` sin cambio / backend: null = sin cambio | Sí (ajuste mínimo, a acordar) |
| W-08 | 🟡 | Enviar `programaId` y exigirlo a ESTUDIANTE | No |
| W-09 | 🟡 | Quitar ID/código/ubicación; mostrar descripción (10-500) | No |
| W-10 | 🟡 | Quitar `URGENTE`; leyenda con 5 estados reales | No |
| W-11 | 🟡 | Lugar opcional con aviso | No |
| W-12 | 🟡 | Quitar firma/QR; comentarios vía `/comentarios`; `nombrePrograma` | No |
| W-13 | ⚪ | Backlog Angular + exponer `mis-solicitudes` | Sí (1 ruta) |
| W-14 | 🟠 | Cerrar POST público, mantener GET programas documentado | Sí (SecurityConfig) |
| W-15 | 🟡 | Handler `HttpMessageNotReadable` uniforme | Sí (1 handler) |
| W-16 | ⚪ | Interfaces exactas, sin fallbacks | No |

---
*Documento de corroboración: si el informe de tu amigo lista el "código" de programa/módulo y la "ubicación" de sede como sobrantes, queda confirmado (W-09); si afirma que Aceptar/Rechazar, crear solicitud o inactivar usuarios "funcionan", eso ya no es así contra el backend actual (W-02–W-06).*

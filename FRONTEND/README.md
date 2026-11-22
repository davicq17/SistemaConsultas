# FRONTEND · COM30 Entrega 2

SPA del **Sistema de Consultas** para la Entrega 2 del proyecto integrador. Amplía la Entrega 1 con
tres vistas con ruta propia, detalle por identificador, alta con formulario reactivo validado y
búsqueda reactiva sobre datos locales del inventario de recursos físicos.

Grupo 1 · Braynne Romero, David Carrillo, Hellen Arrieta.

## Arranque

```bash
pnpm install
pnpm start      # http://localhost:4200
pnpm build      # equivalente a npx ng build
pnpm test       # Vitest
```

Los datos del inventario viven en `src/app/features/recursos/services/recursos.service.ts` y se
conservan en memoria durante la sesión; no se necesita el backend de MySQL para recorrer la SPA.

## Tabla de rutas

| Ruta | Vista | Parámetro |
| --- | --- | --- |
| `/` | Redirección a `/auth/login` | — |
| `/auth/login` | `LoginPage` | — |
| `/auth/register` | `RegisterPage` | — |
| `/users/list` | `UserListPage` | — |
| `/recursos` | `RecursosListPage` | — |
| `/recursos/nuevo` | `RecursoCreatePage` | — |
| `/recursos/:id` | `RecursoDetailPage` | `id`, enlazado con `input.required()` y `numberAttribute` |
| `/404` | `NotFoundPage` | — |
| `/**` | Redirección a `/404` | — |

Las rutas de recursos se cargan de forma diferida con `loadChildren` en `app.routes.ts` y
`loadComponent` en `recursos.routes.ts`. El parámetro de `/recursos/:id` llega al componente gracias a
`withComponentInputBinding()` en `app.config.ts`. Si el id no existe se muestra un mensaje dentro de
la propia vista de detalle; una ruta inexistente cae en `/404`.

## ¿Por qué formulario reactivo y no uno basado en plantillas?

El alta usa `FormBuilder` tipado (`fb.nonNullable.group`) porque el modelo del formulario
(`RecursoFisicoFormValue`) es distinto del modelo de datos (`RecursoFisicoResponse`): el primero
sólo contiene lo que captura el usuario y el segundo incluye `id`, `bloqueNombre` y `activo`, que
genera el servicio. Con un formulario reactivo cada control es explícito, se pueden combinar cuatro
validadores integrados (`required`, `minLength`, `maxLength` y `min`) con el validador propio
`iniciaConMayuscula`, y el estado `invalid`/`touched`/`dirty` gobierna directamente los mensajes y el
botón deshabilitado, sin depender de directivas de plantilla ni de referencias locales. Además, el
valor tipado se convierte al request del servicio en
`features/recursos/mappers/recurso-fisico.mapper.ts`, y `form.reset()` deja el formulario listo para
otra entrada inmediatamente después del alta.

Un formulario basado en plantillas habría exigido dispersar la validación en atributos y variables de
plantilla, dificultando tanto la conversión al modelo de datos como las pruebas unitarias de los
validadores y del mapper.

## Búsqueda reactiva

`RecursosListPage` escucha `busqueda.valueChanges` con `debounceTime(300)` y
`distinctUntilChanged()`, y convierte ese flujo en signal con `toSignal`. No hay `subscribe()`
manual: Angular cancela la suscripción al destruir el componente. El listado se deriva con
`computed()` de la signal de solo lectura expuesta por `RecursosService`, por lo que un alta nuevo
aparece sin recargar la página.

## Estructura relevante

```
src/app/features/recursos/
├── data/             catálogo de bloques
├── mappers/          modelo de formulario → modelo de datos
├── models/           modelo del formulario
├── pages/            listado, alta y detalle
├── services/         inventario local (signal privada + signal de solo lectura)
├── utils/            filtrado por texto
└── validators/       validador propio
```

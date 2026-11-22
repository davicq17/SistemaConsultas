import { Routes } from '@angular/router';

export const RECURSOS_ROUTES: Routes = [
  {
    path: '',
    title: 'Recursos físicos',
    loadComponent: () =>
      import('./pages/recursos-list/recursos-list.page').then((m) => m.RecursosListPage),
  },
  {
    // 'nuevo' debe ir antes que ':id' para no interpretarlo como identificador.
    path: 'nuevo',
    title: 'Nuevo recurso',
    loadComponent: () =>
      import('./pages/recurso-create/recurso-create.page').then((m) => m.RecursoCreatePage),
  },
  {
    path: ':id',
    title: 'Detalle del recurso',
    loadComponent: () =>
      import('./pages/recurso-detail/recurso-detail.page').then((m) => m.RecursoDetailPage),
  },
];

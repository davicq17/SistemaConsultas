import { Routes } from '@angular/router';

export const routes: Routes = [
    {path: '' , redirectTo: 'auth/login', pathMatch:'full'},
    {path: 'auth', 
        loadChildren: () =>
        import('./features/auth/auth.routes').then(m=> m.AUTH_ROUTES)
    },
    {path: 'users',
        loadChildren: () =>
            import('./features/usuarios/users.routes').then(m => m.USER_ROUTES)
    },
    {path: '404',
        loadComponent: () =>
            import('./features/not-found/pages/not-found.page/not-found.page')
                .then(m => m.NotFoundPage)
    },
    {path: '**', redirectTo: '404'}
];

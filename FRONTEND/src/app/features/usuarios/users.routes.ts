import { Routes } from "@angular/router";

export const USER_ROUTES: Routes = [
    {
        path: 'list',
        loadComponent: () => 
            import('./pages/user-list/user-list.page/user-list.page')
                .then(m => m.UserListPage)
    }
]
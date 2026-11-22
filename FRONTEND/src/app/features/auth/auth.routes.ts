import { Routes } from "@angular/router";
import { LoginPage } from "./pages/login/login.page";

export const AUTH_ROUTES: Routes = [
    {
        path: 'login',
        component: LoginPage
    },
    {
        path: 'register',
        loadComponent: () =>
            import('./pages/register/register.page')
                .then(m => m.RegisterPage)
    }
]
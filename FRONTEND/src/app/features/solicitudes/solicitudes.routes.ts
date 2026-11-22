import { Routes } from "@angular/router";
import { MisSolicitudesPage } from "./pages/mis-solicitudes/mis-solicitudes.page/mis-solicitudes.page";
import { SolicitudesListPage } from "./pages/solicitudes-list/solicitudes-list.page.ts/solicitudes-list.page";

export const SOLICITUDES_ROUTES: Routes = [
    {
        path: 'consultas',
        component: SolicitudesListPage
    },
    {
        path: 'solicitudes',
        component: MisSolicitudesPage
    }
]
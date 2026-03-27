import { Routes } from '@angular/router';
import { LandingPage } from './pages/landing-page/landing-page';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { authGuard } from './guard/auth-guard';
import { Error404 } from './pages/error404/error404';
import { PerfilUsuario } from './pages/perfil-usuario/perfil-usuario';
import { PageInicio } from './pages/page-inicio/page-inicio';
import { PageSolicitudes } from './pages/page-solicitudes/page-solicitudes';
import { PageDocumentos } from './pages/page-documentos/page-documentos';
import { PageAnuncios } from './pages/page-anuncios/page-anuncios';
import { PageRegister } from './pages/page-register/page-register';
import { roleGuard } from './guard/rol-guard';

export const routes: Routes = [
    {path: '', pathMatch: 'full', redirectTo: 'landingPage'},
    {path: 'landingPage', component: LandingPage},
    {path: 'login', component: Login},
    {
        path: 'dashboard', component: Dashboard, canActivate: [authGuard], children:
        [
            {path: '', pathMatch:'full', redirectTo: 'pageInicio'},
            {path: 'perfilUsuario', component: PerfilUsuario},
            {path: 'pageInicio', component: PageInicio},
            {path: 'pageSolicitudes', component: PageSolicitudes},
            {path: 'pageDocumentos', component: PageDocumentos},
            {path: 'pageAnuncios', component: PageAnuncios},
            {path: 'pageRegister', component: PageRegister, canActivate: [roleGuard], data: {rol: 'admin'}}
        ]
    },
    {path: 'error404', component: Error404},
    {path: '**', redirectTo: 'error404' }
];

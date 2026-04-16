import { Routes } from '@angular/router';
import { LandingPage } from './pages/landing-page/landing-page';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { authGuard } from './guard/auth-guard';
import { Error404 } from './pages/error404/error404';
import { PerfilUsuario } from './pages/perfil-usuario/perfil-usuario';
import { PageInicio } from './pages/page-inicio/page-inicio';
import { PageSolicitudes } from './pages/page-solicitudes/page-solicitudes';
import { PageRegister } from './pages/page-register/page-register';
import { roleGuard } from './guard/rol-guard';
import { PageUsuarios } from './pages/page-usuarios/page-usuarios';
import { PageDocumentos } from './pages/page-documentos/page-documentos';
import { PageHorarios } from './pages/page-horarios/page-horarios';
import { PageFichajes } from './pages/page-fichajes/page-fichajes';

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
            {path: 'pageUsuarios', component: PageUsuarios},
            {path: 'pageDocumentos', component: PageDocumentos},
            {path: 'pageRegister', component: PageRegister, canActivate: [roleGuard], data: {rol: 'admin'}},
            {path: 'pageHorarios', component: PageHorarios},
            {path: 'pageFichajes', component: PageFichajes}
        ]
    },
    {path: 'error404', component: Error404},
    {path: '**', redirectTo: 'error404' }
];

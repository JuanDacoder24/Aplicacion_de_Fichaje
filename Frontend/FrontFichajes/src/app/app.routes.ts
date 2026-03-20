import { Routes } from '@angular/router';
import { LandingPage } from './pages/landing-page/landing-page';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { authGuard } from './guard/auth-guard';
import { Error404 } from './pages/error404/error404';

export const routes: Routes = [
    {path: '', pathMatch: 'full', redirectTo: 'landingPage'},
    {path: 'landingPage', component: LandingPage},
    {path: 'login', component: Login},
    {
        path: 'dashboard', component: Dashboard, canActivate: [authGuard], children:
        [
            
        ]
    },
    {path: 'error404', component: Error404},
    {path: '**', redirectTo: 'error404' }
];

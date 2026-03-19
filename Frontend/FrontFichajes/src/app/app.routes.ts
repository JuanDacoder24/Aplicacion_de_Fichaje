import { Routes } from '@angular/router';
import { LandingPage } from './pages/landing-page/landing-page';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { authGuard } from './guard/auth-guard';

export const routes: Routes = [
    {path: '', pathMatch: 'full', redirectTo: 'landingPage'},
    {path: 'landingPage', component: LandingPage},
    {path: 'login', component: Login},
    {
        path: 'dashboard', component: Dashboard, canActivate: [authGuard], children:
        [
            
        ]
    }
];

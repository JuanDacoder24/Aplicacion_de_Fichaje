import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

export const roleGuard: CanActivateFn = (route) => {

  const router = inject(Router)
  const token = localStorage.getItem('token')
  const authService = inject(AuthService)

  const requiredRole = route.data['role'] as string;
  if (requiredRole && !authService.isAdmin()) {
    //crear pagina de no tienes permisos
    //router.navigate(['/unauthorized']);
    return false;
  }

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};

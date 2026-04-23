import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import Swal from 'sweetalert2';

export const roleGuard: CanActivateFn = (route, state) => {

  const router = inject(Router)
  const authService = inject(AuthService)
  const token = localStorage.getItem('token')


  if (!token) {
    router.navigate(['/login'])
    return false
  }

  const requiredRole = route.data['rol'] as string

  if (!requiredRole) {
    return true
  }

  const hasRequiredRole = authService.hasRole(requiredRole)

  if (!hasRequiredRole) {

    if (state.url.includes('pageRegister') || ('pageHorarios') || ('pageFichajes')) {
      router.navigate(['/dashboard/pageInicio'])
    } else {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        background: '#0d2a4a',
        color: 'white',
        confirmButtonColor: '#27ae60',
        text: "Lo sentimos, no tienes acceso",
      });
    }
    return false
  }

  return true
}
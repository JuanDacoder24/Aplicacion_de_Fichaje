import { HttpInterceptorFn } from '@angular/common/http'
import { inject } from '@angular/core'
import { Router } from '@angular/router'
import { catchError, throwError } from 'rxjs'

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token')
  const router = inject(Router)
  const publicRoutes = ['/api/auth/login', '/api/auth/register']
  const isPublic = publicRoutes.some(route => req.url.includes(route))

  if (isPublic) {
    return next(req)
  }

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req

  return next(authReq).pipe(
    catchError(err => {
      if (err.status === 401) {
        localStorage.clear()
        router.navigate(['/login'])
      }
      if (err.status === 403) {
        console.error('No tienes permisos para acceder a este recurso');
      }
      return throwError(() => err)
    })
  )
}
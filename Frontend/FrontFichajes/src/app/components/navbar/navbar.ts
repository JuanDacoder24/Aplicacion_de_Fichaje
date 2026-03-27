import { Component, effect, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  
  authService = inject(AuthService)
  private router = inject(Router)
  
  rolActual: string = ''
  esAdmin: boolean = false

  constructor() {
    effect(() => {
      this.rolActual = this.authService.rol()
      this.esAdmin = this.authService.isAdmin()
    })
  }

  ngOnInit() {
    this.rolActual = localStorage.getItem('rol') || 'empleado'
  }

  cambiarRol(event: any) {
    const nuevoRol = event.target.value
    const oldRol = this.rolActual
    this.authService.cambiarRol(nuevoRol)
    
    if (oldRol === 'admin' && nuevoRol === 'empleado') {
      if (this.router.url.includes('pageRegister')) {
        this.router.navigate(['/dashboard/pageInicio'])
      }
    }
  }

  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('rol')
    this.router.navigate(['/landingPage'])
  }
}
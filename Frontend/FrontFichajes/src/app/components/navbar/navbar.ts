import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  
  authService = inject(AuthService);
  private router = inject(Router);
  
  rolActual: string = 'empleado';

  ngOnInit() {
    // Obtener el rol actual al iniciar
    this.rolActual = localStorage.getItem('rol') || 'empleado';
  }

  cambiarRol(event: any) {
    const nuevoRol = event.target.value;
    this.rolActual = nuevoRol;
    localStorage.setItem('rol', nuevoRol);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('rol');
    this.router.navigate(['/landingPage']);
  }
}
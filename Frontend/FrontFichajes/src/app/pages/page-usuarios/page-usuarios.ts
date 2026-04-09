import { Component, signal, inject, OnInit } from '@angular/core';
import { IUsuario } from '../../interface/iusuario';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-page-usuarios',
  imports: [],
  templateUrl: './page-usuarios.html',
  styleUrl: './page-usuarios.css',
})
export class PageUsuarios implements OnInit {

  private authService = inject(AuthService);

  public usuarios = signal<IUsuario[]>([]);
  public isCargando = signal<boolean>(false);

  public departamento = [
    { id: 1, nombre: 'RRHH' },
    { id: 2, nombre: 'IT' },
    { id: 3, nombre: 'VENTAS' },
    { id: 4, nombre: 'MARKETING' }
  ];

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  private async cargarUsuarios() {
    this.isCargando.set(true);
    try {
      const usuarios = await this.authService.getUsuarios(); // 👈 método que debes tener/crear
      this.usuarios.set(usuarios);
    } catch (error) {
      console.error('Error al cargar usuarios:', error);
    } finally {
      this.isCargando.set(false);
    }
  }

  getDepartamento(id: number): string {
    return this.departamento.find(d => d.id === id)?.nombre || '-';
  }
}
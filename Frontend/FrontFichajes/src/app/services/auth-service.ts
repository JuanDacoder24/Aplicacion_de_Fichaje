import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { IUser } from '../interface/iuser';
import { IUsuario, IUsuarioRegistro } from '../interface/iusuario';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private httpClient = inject(HttpClient);

  //he tenido que dividir las rutas para las distintas peticiones
  private apiUrl = 'http://localhost:8080/api';
  private authUrl = `${this.apiUrl}/auth`;
  private usuariosUrl = `${this.apiUrl}/usuarios`;

  private rolSignal = signal<string>('');
  private tokenSignal = signal<string>('');

  constructor() {
    const rol = localStorage.getItem('rol') || '';
    const token = localStorage.getItem('token') || '';
    this.rolSignal.set(rol);
    this.tokenSignal.set(token);
  }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.tokenSignal()}`
    });
  }



  get rol() {
    return this.rolSignal.asReadonly();
  }

  get token() {
    return this.tokenSignal.asReadonly();
  }

  async login(user: IUser): Promise<any> {
  const res = await firstValueFrom(
    this.httpClient.post<any>(`${this.authUrl}/login`, user)
  );
  if (res.token && res.rol) this.setAuthData(res.token, res.rol);
  return res;
}

async register(usuario: IUsuarioRegistro): Promise<any> {
  return await firstValueFrom(
    this.httpClient.post<any>(`${this.authUrl}/register`, usuario, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.tokenSignal()}`
      })
    })
  );
}

async getUsuarios(): Promise<IUsuario[]> {
  return await firstValueFrom(
    this.httpClient.get<IUsuario[]>(this.usuariosUrl, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.tokenSignal()}`
      })
    })
  );
}

  setAuthData(token: string, rol: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('rol', rol);
    this.rolSignal.set(rol);
    this.tokenSignal.set(token);
  }

  cambiarRol(nuevoRol: string) {
    if (nuevoRol === 'admin' || nuevoRol === 'empleado') {
      localStorage.setItem('rol', nuevoRol);
      this.rolSignal.set(nuevoRol);
    } else {
      console.warn('Rol no válido:', nuevoRol);
    }
  }

  getRolActual(): string {
    return this.rolSignal();
  }

  hasRole(role: string): boolean {
    const rolActual = this.getRolActual();
    return rolActual.toLowerCase() === role.toLowerCase();
  }

  isAdmin(): boolean {
    return this.hasRole('admin');
  }

  isEmpleado(): boolean {
    return this.hasRole('empleado');
  }
}
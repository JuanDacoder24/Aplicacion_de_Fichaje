import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { IUser } from '../interface/iuser';
import { IUsuario, IUsuarioRegistro } from '../interface/iusuario';
import { Rol } from '../enum/rol';

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

  //guardo estos datos en el localStorage para no perder los datos si en caso se recarga la pantalla 
  private idSignal = signal<number>(Number(localStorage.getItem('id')) || 0);
  private nombreSignal = signal<string>(localStorage.getItem('nombre') || '');

  constructor() {
    const rol = localStorage.getItem('rol') || '';
    const token = localStorage.getItem('token') || '';
    this.rolSignal.set(rol);
    this.tokenSignal.set(token);
  }

  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.tokenSignal()}`,
    });
  }

  get rol() {
    return this.rolSignal.asReadonly();
  }

  get token() {
    return this.tokenSignal.asReadonly();
  }

  get id() {
    return this.idSignal.asReadonly();
  }

  get nombre() {
    return this.nombreSignal.asReadonly();
  }

  async login(user: IUser): Promise<any> {
  const res = await firstValueFrom(this.httpClient.post<any>(`${this.authUrl}/login`, user));
  if (res.token && res.rol) {
    this.setAuthData(res.token, res.rol, res.id, res.nombre);
    
    const userComplete = {
      id: res.id,
      nombre: res.nombre,
      email: res.email || '',
      rol: res.rol,
      token: res.token
    };
    localStorage.setItem('user', JSON.stringify(userComplete));
  }
  return res;
}

  async register(usuario: IUsuarioRegistro): Promise<any> {
    return await firstValueFrom(
      this.httpClient.post<any>(`${this.authUrl}/register`, usuario, {
        headers: new HttpHeaders({ Authorization: `Bearer ${this.tokenSignal()}` }),
      }),
    );
  }

  async getUsuarios(): Promise<IUsuario[]> {
    return await firstValueFrom(
      this.httpClient.get<IUsuario[]>(this.usuariosUrl, {
        headers: new HttpHeaders({ Authorization: `Bearer ${this.tokenSignal()}` }),
      }),
    );
  }

  setAuthData(token: string, rol: string, id: number, nombre: string) {
  localStorage.setItem('token', token)
  localStorage.setItem('rol', rol)
  localStorage.setItem('id', String(id))
  localStorage.setItem('nombre', nombre)
  
  const userObject = {
    id: id,
    nombre: nombre,
    rol: rol,
    email: '' 
  };
  localStorage.setItem('user', JSON.stringify(userObject))
  
  this.tokenSignal.set(token)
  this.rolSignal.set(rol)
  this.idSignal.set(id)
  this.nombreSignal.set(nombre)
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

getCurrentUser(): Promise<IUsuario | null> {
  const userStr = localStorage.getItem('user');
  
  if (userStr) {
    try {
      const user = JSON.parse(userStr);
      console.log('Usuario desde user:', user);
      return Promise.resolve(user);
    } catch (e) {
      console.error('Error parseando user:', e);
    }
  }
  
  const id = localStorage.getItem('id');
  const nombre = localStorage.getItem('nombre');
  const rol = localStorage.getItem('rol');
  
  if (id && nombre && rol) {
    const user: IUsuario = {
      id: parseInt(id),
      nombre: nombre,
      rol: rol === 'ADMIN' ? Rol.ADMIN : Rol.EMPLEADO,
      email: '',
      passwordHash: '',
      departamento: 0,
      fechaAlta: 0,
      activo: true
    };
    console.log('Usuario construido desde campos:', user);
    return Promise.resolve(user);
  }
  
  return Promise.resolve(null);
}
}

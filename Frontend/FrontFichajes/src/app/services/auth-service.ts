import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { IUser } from '../interface/iuser';
import { IUsuario } from '../interface/iusuario';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private httpClient = inject(HttpClient)
  private baseUrl = 'http://localhost:8080/api/auth'

  private rolSignal = signal<string>('')
  private tokenSignal = signal<string>('')

  constructor() {
    const rol = localStorage.getItem('rol') || ''
    const token = localStorage.getItem('token') || ''
    this.rolSignal.set(rol)
    this.tokenSignal.set(token)
  }

  get rol() {
    return this.rolSignal.asReadonly()
  }

  get token() {
    return this.tokenSignal.asReadonly()
  }

  async login(user: IUser): Promise<any> {
    const res = await firstValueFrom(
      this.httpClient.post<any>(`${this.baseUrl}/login`, user)
    )

    if (res.token && res.rol) {
      this.setAuthData(res.token, res.rol)
    }

    return res

  }

  async register(usuario: IUsuario): Promise<any> {
    const res = await firstValueFrom(
      this.httpClient.post<any>(`${this.baseUrl}/register`, usuario)
    )
    return res

  }

  setAuthData(token: string, rol: string) {
    localStorage.setItem('token', token)
    localStorage.setItem('rol', rol)
    this.rolSignal.set(rol)
    this.tokenSignal.set(token)
  }

  cambiarRol(nuevoRol: string) {
    if (nuevoRol === 'admin' || nuevoRol === 'empleado') {
      localStorage.setItem('rol', nuevoRol)
      this.rolSignal.set(nuevoRol)
    } else {
      console.warn('Rol no válido:', nuevoRol)
    }
  }

  getRolActual(): string {
    return this.rolSignal()
  }

  hasRole(role: string): boolean {
    const rolActual = this.getRolActual()
    return rolActual.toLowerCase() === role.toLowerCase()
  }

  isAdmin(): boolean {
    return this.hasRole('admin')
  }

  isEmpleado(): boolean {
    return this.hasRole('empleado')
  }

}
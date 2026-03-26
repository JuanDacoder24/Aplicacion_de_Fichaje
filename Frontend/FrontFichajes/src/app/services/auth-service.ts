import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { IUsuario } from '../interface/iusuario';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private rolSubject = new BehaviorSubject<string>('');

  // Al cargar la aplicacion, esto lee el rol del localStorage
  constructor() {
    const rol = localStorage.getItem('rol') || '';
    this.rolSubject.next(rol);
  }

  setAuthData(token: string, rol: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('rol', rol);
    this.rolSubject.next(rol);
  }

  getRol(): string {
    return this.rolSubject.value;
  }

  hasRole(role: string): boolean {
    return this.getRol() === role;
  }

  isAdmin(): boolean {
    const rol = localStorage.getItem('rol');
    return rol === 'admin';
  }




}

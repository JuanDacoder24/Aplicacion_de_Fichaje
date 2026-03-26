import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IUser } from '../interface/iuser';
import { firstValueFrom } from 'rxjs';
import { IUsuario } from '../interface/iusuario';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private httpClient = inject(HttpClient)
  private baseUrl = 'http://localhost:8080/api'

  async login(user: IUser): Promise<any>{
    const res= await firstValueFrom(this.httpClient.post<any>(`${this.baseUrl}/auth/login`, user))
    return res
  }

  async register(usuario: IUsuario): Promise<any>{
    const res= await firstValueFrom(this.httpClient.post<any>(`${this.baseUrl}/auth/register`, usuario))
    return res
  }
}

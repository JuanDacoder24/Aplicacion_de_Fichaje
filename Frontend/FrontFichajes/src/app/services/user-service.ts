import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IUser } from '../interface/iuser';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private httpClient = inject(HttpClient)
  private baseUrl = 'http://localhost:8080/api/auth/login'

  login(user: IUser): Promise<any>{
    return firstValueFrom(this.httpClient.post<any>(this.baseUrl, user))
  }
}

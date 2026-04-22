import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Injectable, inject, signal } from '@angular/core'
import { IFichajes } from '../interface/ifichajes'
import { IHorarios } from '../interface/ihorarios'
import { ISolicitudes } from '../interface/isolicitudes';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { IUsuario } from '../interface/iusuario';
import { AuthService } from './auth-service';

@Injectable({
  providedIn: 'root',
})
export class FichajeService {

  private httpClient = inject(HttpClient)
  private authService = inject(AuthService)

  private apiUrl = 'http://localhost:8080/api'
  private fichajesUrl = `${this.apiUrl}/fichajes`
  private horariosUrl = `${this.apiUrl}/horarios`
  private solicitudesUrl = `${this.apiUrl}/solicitudes`

  private tokenSignal = signal<string>(localStorage.getItem('token') || '')

  //Esto construye la cabecera HTTP que el backend espera para verificar que estás autenticado.
  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.tokenSignal()}`
    })
  }

  async getUsuarios(): Promise<IUsuario[]> {
    return await firstValueFrom(this.httpClient.get<IUsuario[]>(`${this.apiUrl}/usuarios`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async getUsuarioById(id: number): Promise<IUsuario> {
    return await firstValueFrom(this.httpClient.get<IUsuario>(`${this.apiUrl}/usuarios/${id}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async updateUserById(user: IUsuario): Promise<IUsuario> {
    return await lastValueFrom(this.httpClient.put<IUsuario>(`${this.apiUrl}/${user.id}`, user))
  }

  // FICHAJES 

  async getFichajes(page = 0, size = 100): Promise<any> {
    return await firstValueFrom(this.httpClient.get<any>(`${this.fichajesUrl}?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async getFichajesByUsuario(usuarioId: number, page = 0, size = 100): Promise<any> {
    return await firstValueFrom(this.httpClient.get<any>(`${this.fichajesUrl}/${usuarioId}?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async createFichaje(tipo: 'ENTRADA' | 'SALIDA'): Promise<any> {
    const ahora = new Date();

    const fechaSolo = ahora.toISOString().split('T')[0]; 

    const body = {
      usuarioId: this.authService.id(),
      tipo: tipo,
      fecha: new Date().toISOString().split('T')[0],
      descansoMinutos: 0
    };

    console.log('Enviando:', body);

    return await firstValueFrom(
      this.httpClient.post<any>(this.fichajesUrl, body, {
        headers: this.getAuthHeaders()
      })
    );
  }
  async updateFichaje(id: number, fichaje: IFichajes): Promise<IFichajes> {
    return await firstValueFrom(this.httpClient.put<IFichajes>(`${this.fichajesUrl}/${id}`, fichaje, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async deleteFichaje(id: number): Promise<any> {
    return await firstValueFrom(this.httpClient.delete<any>(`${this.fichajesUrl}/${id}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async getFichajesPorRango(inicio: string, fin: string): Promise<any> {
    return await firstValueFrom(this.httpClient.get<any>(`${this.fichajesUrl}/rango?inicio=${inicio}&fin=${fin}`,
      {
        headers: this.getAuthHeaders()

      })
    )
  }

  async getFichajeAbierto(usuarioId: number, fecha: string): Promise<any> {
    return await firstValueFrom(this.httpClient.get<any>(
      `${this.fichajesUrl}/abierto?usuarioId=${usuarioId}&fecha=${fecha}`,
      { headers: this.getAuthHeaders() }
    ));
  }

  // HORARIOS 

  async getHorarios(): Promise<IHorarios[]> {
    return await firstValueFrom(this.httpClient.get<IHorarios[]>(this.horariosUrl, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async createHorario(horario: any): Promise<any> {
    try {
      const response = await fetch('http://localhost:8080/api/horarios', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(horario)
      });

      if (!response.ok) {
        const error = await response.text();
        console.error('Error response:', error);
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error en createHorario:', error);
      throw error;
    }
  }

  async updateHorario(id: number, horario: IHorarios): Promise<IHorarios> {
    return await firstValueFrom(this.httpClient.put<IHorarios>(`${this.horariosUrl}/${id}`, horario, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async deleteHorario(id: number): Promise<any> {
    return await firstValueFrom(this.httpClient.delete<any>(`${this.horariosUrl}/${id}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  // SOLICITUDES 

  async getSolicitudes(page = 0, size = 100): Promise<any> {
    return await firstValueFrom(this.httpClient.get<any>(`${this.solicitudesUrl}?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async getSolicitudById(id: number): Promise<ISolicitudes> {
    return await firstValueFrom(this.httpClient.get<ISolicitudes>(`${this.solicitudesUrl}/${id}`, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async createSolicitud(solicitud: ISolicitudes): Promise<ISolicitudes> {
    return await firstValueFrom(this.httpClient.post<ISolicitudes>(this.solicitudesUrl, solicitud, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async updateSolicitud(id: number, solicitud: ISolicitudes): Promise<ISolicitudes> {
    return await firstValueFrom(this.httpClient.put<ISolicitudes>(`${this.solicitudesUrl}/${id}`, solicitud, {
      headers: this.getAuthHeaders()
    })
    )
  }

  async deleteSolicitud(id: number): Promise<any> {
    return await firstValueFrom(this.httpClient.delete<any>(`${this.solicitudesUrl}/${id}`, {
      headers: this.getAuthHeaders()
    })
    )
  }
}
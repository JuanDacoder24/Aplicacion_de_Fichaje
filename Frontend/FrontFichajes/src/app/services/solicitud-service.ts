import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ISolicitudes } from '../interface/isolicitudes';
import { IJustificante } from '../interface/ijustificante';

@Injectable({
  providedIn: 'root'
})

export class SolicitudService {

  private http = inject(HttpClient)
  private apiSolicitudes = '/api/solicitudes'
  private apiJustificantes = '/api/justificantes'


  obtenerSolicitudes(estado?: string): Promise<ISolicitudes[]> {
    let params = new HttpParams()
    if (estado) params = params.set('estado', estado)
    return firstValueFrom(
      this.http.get<ISolicitudes[]>(this.apiSolicitudes, { params })
    )
  }

  obtenerJustificantes(): Promise<IJustificante[]> {
    return firstValueFrom(
      this.http.get<IJustificante[]>(this.apiJustificantes)  // GET /api/justificantes
    ).catch(error => {
      console.error('Error obteniendo justificantes:', error);
      throw error;
    });
  }

  crearSolicitud(motivo: string, fichajeId?: number): Promise<ISolicitudes> {
    const body: any = { motivo }
    if (fichajeId) body.fichajeId = fichajeId

    console.log('POST a /api/solicitudes con:', body)

    return firstValueFrom(
      this.http.post<ISolicitudes>(this.apiSolicitudes, body)
    ).then(response => {
      console.log('Respuesta del servidor:', response)
      return response
    }).catch(error => {
      console.error('Error en petición:', error)
      throw error
    })
  }

  revisarSolicitud(id: number, estado: string, comentario: string): Promise<ISolicitudes> {
    console.log(`Revisando solicitud ${id} con estado ${estado} y comentario:`, comentario);

    return firstValueFrom(
      this.http.put<ISolicitudes>(`${this.apiSolicitudes}/${id}/revisar`,
        { estado, comentario }
      )
    ).catch(error => {
      console.error(`Error al revisar solicitud ${id}:`, error);
      let errorMsg = 'Error al revisar la solicitud';

      if (error.status === 403) {
        errorMsg = 'No tienes permisos para revisar solicitudes';
      } else if (error.status === 404) {
        errorMsg = 'Solicitud no encontrada';
      } else if (error.status === 500) {
        errorMsg = 'Error interno del servidor. Contacta con el administrador.';
      }

      throw new Error(errorMsg);
    });
  }

  eliminarSolicitud(id: number): Promise<void> {
    return firstValueFrom(this.http.delete<void>(`${this.apiSolicitudes}/${id}`))
  }


  obtenerPendientes(): Promise<IJustificante[]> {
    return firstValueFrom(
      this.http.get<IJustificante[]>(`${this.apiJustificantes}/pendientes`)
    )
  }

  subirJustificante(
    archivo: File,
    tipoDocumento: string,
    solicitudId?: number,
    fichajeId?: number
  ): Promise<{ id: number; mensaje: string }> {
    const form = new FormData()
    form.append('archivo', archivo)
    form.append('tipoDocumento', tipoDocumento)
    if (solicitudId) form.append('solicitudId', solicitudId.toString())
    if (fichajeId) form.append('fichajeId', fichajeId.toString())

    return firstValueFrom(
      this.http.post<{ id: number; mensaje: string }>(
        `${this.apiJustificantes}`, form
      )
    )
  }

  revisarJustificante(id: number, estado: string, comentario: string): Promise<IJustificante> {
    return firstValueFrom(
      this.http.put<IJustificante>(`${this.apiJustificantes}/${id}/revisar`,
        { estado, comentario }
      )
    )
  }

  verPdf(justificanteId: number): string {
    return `/api/justificantes/${justificanteId}/ver`;
  }

  obtenerMisSolicitudes(): Promise<ISolicitudes[]> {
    return firstValueFrom(
      this.http.get<ISolicitudes[]>(`${this.apiSolicitudes}/mis-solicitudes`)
    )
  }
}
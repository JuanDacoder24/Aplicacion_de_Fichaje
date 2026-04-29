import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ISolicitudes } from '../interface/isolicitudes';
import { IJustificante } from '../interface/ijustificante';

@Injectable({ 
  providedIn: 'root' 
})

export class SolicitudService {

  private http = inject(HttpClient);
  private apiSolicitudes    = '/api/solicitudes';
  private apiJustificantes  = '/api/justificantes';

  
  obtenerSolicitudes(estado?: string): Promise<ISolicitudes[]> {
    let params = new HttpParams();
    if (estado) params = params.set('estado', estado);
    return firstValueFrom(
      this.http.get<ISolicitudes[]>(this.apiSolicitudes, { params })
    );
  }

  crearSolicitud(motivo: string, fichajeId?: number): Promise<ISolicitudes> {
  const body: any = { motivo };
  if (fichajeId) body.fichajeId = fichajeId;
  
  console.log('POST a /api/solicitudes con:', body);
  
  return firstValueFrom(
    this.http.post<ISolicitudes>(this.apiSolicitudes, body)
  ).then(response => {
    console.log('Respuesta del servidor:', response);
    return response;
  }).catch(error => {
    console.error('Error en petición:', error);
    throw error;
  });
}

  revisarSolicitud(id: number, estado: string, comentario: string): Promise<ISolicitudes> {
    return firstValueFrom(
      this.http.put<ISolicitudes>(`${this.apiSolicitudes}/${id}/revisar`, 
        { estado, comentario }
      )
    );
  }

  eliminarSolicitud(id: number): Promise<void> {
    return firstValueFrom(this.http.delete<void>(`${this.apiSolicitudes}/${id}`));
  }

  obtenerJustificantes(): Promise<IJustificante[]> {
    return firstValueFrom(
      this.http.get<IJustificante[]>(this.apiJustificantes)
    );
  }

  obtenerPendientes(): Promise<IJustificante[]> {
    return firstValueFrom(
      this.http.get<IJustificante[]>(`${this.apiJustificantes}/pendientes`)
    );
  }

  subirJustificante(
    archivo: File,
    tipoDocumento: string,
    solicitudId?: number,
    fichajeId?: number
  ): Promise<{ id: number; mensaje: string }> {
    const form = new FormData();
    form.append('archivo', archivo);
    form.append('tipoDocumento', tipoDocumento);
    if (solicitudId) form.append('solicitudId', solicitudId.toString());
    if (fichajeId)   form.append('fichajeId',   fichajeId.toString());

    return firstValueFrom(
      this.http.post<{ id: number; mensaje: string }>(
        `${this.apiJustificantes}`, form
      )
    );
  }

  revisarJustificante(id: number, estado: string, comentario: string): Promise<IJustificante> {
    return firstValueFrom(
      this.http.put<IJustificante>(`${this.apiJustificantes}/${id}/revisar`, 
        { estado, comentario }
      )
    );
  }

  verPdf(justificanteId: number): string {
    return `${this.apiJustificantes}/${justificanteId}/ver`;
  }

  obtenerMisSolicitudes(): Promise<ISolicitudes[]> {
    return firstValueFrom(
      this.http.get<ISolicitudes[]>(`${this.apiSolicitudes}/mis-solicitudes`)
    );
  }
}
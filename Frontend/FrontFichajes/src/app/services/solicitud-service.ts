import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ISolicitudes, RevisionSolicitudDTO, FiltroSolicitudesDTO, EstadisticasSolicitudesDTO } from '../interface/isolicitudes';
import { IJustificante } from '../interface/ijustificante';

@Injectable({
    providedIn: 'root'
})
export class SolicitudService {

    private http = inject(HttpClient)
    
    private apiUrl = '/api/solicitudes'
    private justificantesUrl = '/api/justificantes'

    obtenerTodas(filtros?: FiltroSolicitudesDTO): Promise<ISolicitudes[]> {
        let params = new HttpParams()
        
        if (filtros) {
            if (filtros.estado) params = params.set('estado', filtros.estado)
            if (filtros.fechaInicio) params = params.set('fechaInicio', filtros.fechaInicio.toISOString())
            if (filtros.fechaFin) params = params.set('fechaFin', filtros.fechaFin.toISOString())
            if (filtros.usuarioId) params = params.set('usuarioId', filtros.usuarioId.toString())
        }
        
        return firstValueFrom(this.http.get<ISolicitudes[]>(this.apiUrl, { params }))
    }

    //corregir las rutas y trabajas con los roles para no diferencias entre "mis-solicitudes" y "con-justificante"

    obtenerMisSolicitudes(): Promise<ISolicitudes[]> {
        return firstValueFrom(this.http.get<ISolicitudes[]>(`${this.apiUrl}/mis-solicitudes`))
    }

    obtenerPorUsuario(usuarioId: number): Promise<ISolicitudes[]> {
        return firstValueFrom(this.http.get<ISolicitudes[]>(`${this.apiUrl}/usuario/${usuarioId}`))
    }

    crearSolicitudConJustificante(formData: FormData): Promise<ISolicitudes> {
        return firstValueFrom(this.http.post<ISolicitudes>(`${this.apiUrl}/con-justificante`, formData))
    }

    aprobarSolicitud(id: number, revision: RevisionSolicitudDTO): Promise<ISolicitudes> {
        return firstValueFrom(this.http.patch<ISolicitudes>(`${this.apiUrl}/${id}/aprobar`, revision))
    }

    rechazarSolicitud(id: number, revision: RevisionSolicitudDTO): Promise<ISolicitudes> {
        return firstValueFrom(this.http.patch<ISolicitudes>(`${this.apiUrl}/${id}/rechazar`, revision))
    }

    eliminarSolicitud(id: number): Promise<void> {
        return firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${id}`))
    }

    obtenerEstadisticas(): Promise<EstadisticasSolicitudesDTO> {
        return firstValueFrom(this.http.get<EstadisticasSolicitudesDTO>(`${this.apiUrl}/estadisticas`))
    }

    subirJustificante(archivo: File, tipoDocumento: string, solicitudId?: number, fichajeId?: number): Promise<any> {
        const formData = new FormData()
        formData.append('archivo', archivo)
        formData.append('tipoDocumento', tipoDocumento)
        if (solicitudId) formData.append('solicitudId', solicitudId.toString())
        if (fichajeId) formData.append('fichajeId', fichajeId.toString())
        
        return firstValueFrom(this.http.post(`${this.justificantesUrl}/subir`, formData))
    }

    getJustificantesPendientes(): Promise<IJustificante[]> {
        return firstValueFrom(this.http.get<IJustificante[]>(`${this.justificantesUrl}/pendientes`))
    }

    getMisJustificantes(): Promise<IJustificante[]> {
        return firstValueFrom(this.http.get<IJustificante[]>(`${this.justificantesUrl}/mis-justificantes`))
    }

    obtenerJustificantePorSolicitud(solicitudId: number): Promise<IJustificante> {
        return firstValueFrom(this.http.get<IJustificante>(`${this.justificantesUrl}/solicitud/${solicitudId}`))
    }

    verPdf(justificanteId: number): Promise<Blob> {
        return firstValueFrom(this.http.get(`${this.justificantesUrl}/${justificanteId}/ver`, {
            responseType: 'blob'
        }))
    }

    revisarJustificante(id: number, estado: string, comentario: string): Promise<IJustificante> {
        return firstValueFrom(this.http.put<IJustificante>(`${this.justificantesUrl}/${id}/revisar`, { 
            estado, 
            comentario 
        }))
    }
}
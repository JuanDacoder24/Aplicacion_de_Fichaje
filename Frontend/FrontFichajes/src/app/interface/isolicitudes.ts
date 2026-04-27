import { IFichajes } from "./ifichajes"
import { IJustificante } from "./ijustificante";
import { IUsuario } from "./iusuario"

export interface ISolicitudes {
    id: number
    usuario?: IUsuario
    fichaje?: IFichajes
    motivo: string
    estado: 'PENDIENTE' | 'APROBADA' | 'RECHAZADA'
    fechaSolicitud: Date
    justificante?: IJustificante  
    tipoDocumento?: string
}

export interface CrearSolicitudDTO {
    motivo: string
    fichajeId?: number | null
    tipoDocumento: string 
}

export interface RevisionSolicitudDTO {
    estado: 'APROBADA' | 'RECHAZADA'
    comentarioAdmin?: string
}

export interface CrearJustificanteDTO {
    solicitudId: number
    fichajeId?: number | null
    tipoDocumento: string
    nombreArchivo: string
    rutaArchivo: string
}

export interface FiltroSolicitudesDTO {
    estado?: 'PENDIENTE' | 'APROBADA' | 'RECHAZADA'
    fechaInicio?: Date
    fechaFin?: Date
    usuarioId?: number
}

export interface EstadisticasSolicitudesDTO {
    total: number
    pendientes: number
    aprobadas: number
    rechazadas: number
    porTipoDocumento: { [key: string]: number }
}
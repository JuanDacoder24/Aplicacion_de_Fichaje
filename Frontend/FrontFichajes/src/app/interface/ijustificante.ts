export interface IJustificante {
  id: number;
  usuarioId: number;
  fichajeId?: number | null;
  solicitudId?: number | null;
  nombreArchivo: string;
  tipoDocumento: string;
  rutaArchivo: string;
  estado: 'PENDIENTE' | 'APROBADA' | 'RECHAZADA';
  fechaSubida: string | Date;
  fechaRevision?: string | Date;
  revisadoPor?: number;
  usuario?: {
    id: number;
    nombre: string;
    email: string;
  };
  solicitud?: {
    id: number;
    motivo: string;
    estado: string;
  };
}
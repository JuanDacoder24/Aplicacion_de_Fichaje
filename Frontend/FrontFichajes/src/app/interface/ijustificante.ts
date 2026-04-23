export interface IJustificante {

    id: number;
    usuario_id: number;
    fichaje_id?: number | null;
    solicitud_id?: number | null;
    nombre_archivo: string;
    tipo_documento: string;
    ruta_archivo: string;
    estado: 'PENDIENTE' | 'APROBADO' | 'RECHAZADO';
    comentario_admin?: string;
    fecha_subida: Date;
    fecha_revision?: Date;
    revisado_por?: number;
}

import { IUsuario } from "./iusuario"

export interface IFichajes {

    id: number
    usuario?: IUsuario
    fecha: Date
    horaEntrada: Date
    horaSalida: Date
    descansoMinutos: number
    horasTrabajadas: number
    comentario: string
    tipo?: string

}

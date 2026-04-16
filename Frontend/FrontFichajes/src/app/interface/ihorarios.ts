import { IUsuario } from "./iusuario"

export interface IHorarios {

    id: number
    usuario: IUsuario
    diaSemana: string
    horaInicio: Date
    horaFin: Date

}

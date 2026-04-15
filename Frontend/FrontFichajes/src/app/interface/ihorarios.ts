import { IUsuario } from "./iusuario"

export interface IHorarios {

    id: number
    usuario: IUsuario
    horaInicio: Date
    horaFin: Date
    horasSemanales: number

}

import { IFichajes } from "./ifichajes"
import { IUsuario } from "./iusuario"

export interface ISolicitudes {

    id: number
    usuario: IUsuario
    fichaje: IFichajes
    motivo: string
    estado: string 
    
}

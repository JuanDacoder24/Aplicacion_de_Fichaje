export interface IUsuario {

    id: number
    nombre: string
    email: string
    passwordHash: string
    rol: number
    departamento: number
    fechaAlta: number
    activo: boolean

}

export interface IUsuarioRegistro {
    nombre: string
    email: string
    passwordHash: string
    rol: number
    departamento: number
    activo: boolean
}

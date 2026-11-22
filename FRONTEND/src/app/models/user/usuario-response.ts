import { Rol } from "../enums/rol.enum"

export interface UsuarioResponse {
    id: number,
    identificacion: string,
    nombre: string,
    apellido: string,
    correo: string,
    rol: Rol,
    programaId?: number,
    programaNombre?: string,
    activo: boolean,
    fechaCreacion: Date
}
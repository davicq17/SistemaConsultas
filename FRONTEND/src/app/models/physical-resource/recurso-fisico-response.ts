import { TipoRecursoFisico } from "../enums/tipo-recurso-fisico.enum"

export interface RecursoFisicoResponse {
    id: number,
    nombre: string,
    bloqueId: number,
    bloqueNombre: string,
    tipo: TipoRecursoFisico,
    activo: boolean
}
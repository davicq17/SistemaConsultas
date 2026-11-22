import { TipoRecursoFisico } from "../enums/tipo-recurso-fisico.enum";

export interface RecursoFisicoRequest {
    nombre: string,
    tipo: TipoRecursoFisico,
    bloqueId: number
}
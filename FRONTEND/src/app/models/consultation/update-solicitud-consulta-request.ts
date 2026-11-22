import { PrioridadSolicitud } from "../enums/prioridad-solicitud.enum";

export interface UpdateSolicitudConsultaRequest {
    prioridad: PrioridadSolicitud,
    asunto: string,
    descripcion: string,
    fechaConsulta: Date,
    horaConsulta: string,
    moduloId: number,
    motivo?: string 
}
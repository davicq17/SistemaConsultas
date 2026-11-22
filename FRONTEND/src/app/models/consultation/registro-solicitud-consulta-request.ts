import { PrioridadSolicitud } from "../enums/prioridad-solicitud.enum";

export interface RegistroSolicitudConsultaRequest {
    prioridad: PrioridadSolicitud,
    asunto: string,
    descripcion: string,
    fechaConsulta: Date,
    horaConsulta: string,
    docenteId: number,
    moduloId: number
}
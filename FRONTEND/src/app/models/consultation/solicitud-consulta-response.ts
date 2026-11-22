import { EstadoSolicitud } from "../enums/estado-solicitud.enum";
import { PrioridadSolicitud } from "../enums/prioridad-solicitud.enum";

export interface SolicitudConsultaResponse {
    id: number,
    numeroConsulta: string,
    estado: EstadoSolicitud,
    prioridad: PrioridadSolicitud,
    asunto: string,
    descripcion: string,
    fechaCreacion: Date,
    fechaConsulta: Date,
    horaConsulta: string,
    identificacionEstudiante: string,
    nombreCompletoEstudiante: string,
    identificacionDocente: string, 
    nombreCompletoDocente: string,
    nombreModulo: string,
    nombrePrograma?:string,
    nombreRecursoFisico: String
}
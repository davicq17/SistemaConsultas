import { EstadoSolicitud } from "../enums/estado-solicitud.enum";

export interface CambiarEstadoSolicitudRequest {
    estado: EstadoSolicitud,
    motivo?: string,
}
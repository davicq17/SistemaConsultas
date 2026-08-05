package co.edu.iub.sistemaconsultas.dto.solicitud

import co.edu.iub.sistemaconsultas.model.EstadoSolicitud

data class UpdateSolicitudConsultaRequest(
    val asunto: String?,
    val descripcion: String?,
    val prioridad: String?,
    val estado: EstadoSolicitud?,
    val docenteId: Long?
)
package co.edu.iub.sistemaconsultas.dto.solicitud

import co.edu.iub.sistemaconsultas.model.EstadoSolicitud
import java.time.LocalDateTime

data class SolicitudConsultaResponse(
    val id: Long,
    val asunto: String,
    val descripcion: String,
    val prioridad: String?,
    val estado: EstadoSolicitud,
    val fechaCreacion: LocalDateTime,
    val estudianteId: Long,
    val docenteId: Long?,
    val moduloId: Long
)
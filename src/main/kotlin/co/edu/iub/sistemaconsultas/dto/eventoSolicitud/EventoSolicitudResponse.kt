package co.edu.iub.sistemaconsultas.dto.eventoSolicitud

import co.edu.iub.sistemaconsultas.model.enums.TipoEvento
import java.time.LocalDateTime

data class EventoSolicitudResponse(
    val id: Long,
    val solicitudId: Long,
    val usuarioId: Long,
    val nombreUsuario: String,
    val tipoEvento: TipoEvento,
    val descripcion: String,
    val fechaEvento: LocalDateTime
)

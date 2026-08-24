package co.edu.iub.sistemaconsultas.dto.notificacion

import co.edu.iub.sistemaconsultas.model.enums.TipoNotificacion
import java.time.LocalDateTime

data class NotificacionResponse(

    val id: Long,

    val remitenteId: Long,

    val nombreRemitente: String,

    val nombreDestinatario: String,

    val solicitudConsultaId: Long,

    val numeroConsulta: String,

    val tipo: TipoNotificacion,

    val titulo: String,

    val mensaje: String,

    val leida: Boolean,

    val fechaCreacion: LocalDateTime,
)

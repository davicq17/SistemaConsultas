package co.edu.iub.sistemaconsultas.dto.notificacion

import co.edu.iub.sistemaconsultas.model.enums.TipoNotificacion
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegistroNotificacionRequest (

    @field:NotNull(message = "La notificación debe tener un remitente.")
    val remitenteId: Long,

    @field:NotNull(message = "La notificación debe tener un destinatario.")
    val destinatarioId: Long,

    @field:NotNull(message = "La notificación debe estar relacionada a una consulta.")
    val solicitudConsultaId: Long,

    @field:NotNull(message = "La notificación debe ser de algún tipo.")
    val tipo: TipoNotificacion,

    @field:NotBlank(message = "La notificación debe tener un título.")
    val titulo: String,

    @field:NotBlank(message = "La notificación debe tener un mensaje.")
    val mensaje: String
)
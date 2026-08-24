package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.notificacion.NotificacionResponse
import co.edu.iub.sistemaconsultas.model.Notificacion

fun Notificacion.toResponse(): NotificacionResponse {

    return NotificacionResponse(
        id = id!!,
        remitenteId = remitente.id!!,
        nombreRemitente = "${remitente.nombre} ${remitente.apellido}",
        nombreDestinatario = "${destinatario.nombre} ${destinatario.apellido}",
        solicitudConsultaId = solicitudConsulta.id!!,
        numeroConsulta = solicitudConsulta.numeroConsulta,
        tipo = tipo,
        titulo = titulo,
        mensaje = mensaje,
        leida = leida,
        fechaCreacion = fechaCreacion
    )
}
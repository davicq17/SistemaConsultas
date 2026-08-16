package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.eventoSolicitud.EventoSolicitudResponse
import co.edu.iub.sistemaconsultas.model.EventoSolicitud

fun EventoSolicitud.toResponse(): EventoSolicitudResponse{
    return EventoSolicitudResponse(
        id = id!!,
        solicitudId = solicitud.id!!,
        usuarioId = usuario.id!!,
        nombreUsuario = "${usuario.nombre} ${usuario.apellido}",
        tipoEvento = tipoEvento,
        descripcion = descripcion,
        fechaEvento = fechaEvento
    )
}
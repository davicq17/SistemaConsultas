package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.comentario.ComentarioResponse
import co.edu.iub.sistemaconsultas.model.Comentario

fun Comentario.toResponse(): ComentarioResponse{

    return ComentarioResponse(

        id = id!!,
        contenido = contenido,
        autorId = autor.id!!,
        nombreAutor = "${autor.nombre} ${autor.apellido}",
        consultaId = solicitudConsulta.id!!,
        numeroConsulta = solicitudConsulta.numeroConsulta,
        fechaCreacion = fechaCreacion,
        editado = editado
    )
}
package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.ModuloResponse
import co.edu.iub.sistemaconsultas.model.Modulo

fun Modulo.toResponse(): ModuloResponse {

    return ModuloResponse(

        id = id!!,

        nombre = nombre,

        descripcion = descripcion,

        activo = activo

    )

}
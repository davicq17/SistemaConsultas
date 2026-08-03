package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.sede.SedeResponse
import co.edu.iub.sistemaconsultas.model.Sede

fun Sede.toResponse(): SedeResponse {
    return SedeResponse(
        id = id!!,
        nombre = nombre,
        activo = activo
    )
}
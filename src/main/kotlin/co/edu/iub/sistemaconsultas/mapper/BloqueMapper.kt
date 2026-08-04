package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.bloque.BloqueResponse
import co.edu.iub.sistemaconsultas.model.Bloque

fun Bloque.toResponse(): BloqueResponse{
    return BloqueResponse(
        id = id!!,
        nombre = nombre,
        sedeId = sede.id!!,
        sedeNombre = sede.nombre,
        activo = activo
    )
}
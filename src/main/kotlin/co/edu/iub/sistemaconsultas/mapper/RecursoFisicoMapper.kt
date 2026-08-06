package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.recursoFisico.RecursoFisicoResponse
import co.edu.iub.sistemaconsultas.model.RecursoFisico

fun RecursoFisico.toResponse(): RecursoFisicoResponse{

    return RecursoFisicoResponse(
        id = id!!,
        nombre = nombre,
        bloqueId = bloque.id!!,
        bloqueNombre = bloque.nombre,
        tipo = tipo,
        activo = activo
    )
}
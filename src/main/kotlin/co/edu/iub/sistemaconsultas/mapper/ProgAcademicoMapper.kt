package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.programaAcademico.ProgAcademicoResponse
import co.edu.iub.sistemaconsultas.model.ProgramaAcademico

fun ProgramaAcademico.toResponse(): ProgAcademicoResponse{

    return ProgAcademicoResponse(

        id = id!!,

        nombre = nombre,

        activo = activo
    )
}
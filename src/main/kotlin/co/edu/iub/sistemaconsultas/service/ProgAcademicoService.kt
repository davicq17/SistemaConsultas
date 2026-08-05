package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.programaAcademico.ProgAcademicoResponse
import co.edu.iub.sistemaconsultas.dto.programaAcademico.RegistroProgAcademicoRequest
import co.edu.iub.sistemaconsultas.dto.programaAcademico.UpdateProgAcademicoRequest

interface ProgAcademicoService {

    fun listarProgramas(): List<ProgAcademicoResponse>

    fun obtenerProgramaPorId(id: Long): ProgAcademicoResponse

    fun registrarPrograma(
        request: RegistroProgAcademicoRequest
    ): ProgAcademicoResponse

    fun actualizarPrograma(
        id: Long,
        request: UpdateProgAcademicoRequest
    ): ProgAcademicoResponse

    fun eliminarPrograma(id: Long)

}
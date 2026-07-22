package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.ProgAcademicoResponse
import co.edu.iub.sistemaconsultas.dto.RegistroProgAcademicoRequest
import co.edu.iub.sistemaconsultas.dto.UpdateProgAcademicoRequest

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
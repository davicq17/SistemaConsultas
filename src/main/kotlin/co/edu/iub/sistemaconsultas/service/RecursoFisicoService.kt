package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.recursoFisico.RecursoFisicoResponse
import co.edu.iub.sistemaconsultas.dto.recursoFisico.RegistroRecursoFisicoRequest
import co.edu.iub.sistemaconsultas.dto.recursoFisico.UpdateRecursoFisicoRequest

interface RecursoFisicoService {

    fun registrarRecursoFisico(request: RegistroRecursoFisicoRequest): RecursoFisicoResponse

    fun listarRecursosFisicos(): List<RecursoFisicoResponse>

    fun obtenerRecursoFisicoPorId(id: Long): RecursoFisicoResponse

    fun actualizarRecursoFisico(id: Long, request: UpdateRecursoFisicoRequest): RecursoFisicoResponse

    fun eliminarRecursoFisico(id: Long)
}
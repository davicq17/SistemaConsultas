package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.recursoFisico.RecursoFisicoResponse
import co.edu.iub.sistemaconsultas.dto.recursoFisico.RegistroRecursoFisicoRequest
import co.edu.iub.sistemaconsultas.dto.recursoFisico.UpdateRecursoFisicoRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.RecursoFisico
import co.edu.iub.sistemaconsultas.repository.BloqueRepository
import co.edu.iub.sistemaconsultas.repository.RecursoFisicoRepository
import co.edu.iub.sistemaconsultas.service.RecursoFisicoService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class RecursoFisicoServiceImpl(
    private val bloqueRepository: BloqueRepository,
    private val recursoFisicoRepository: RecursoFisicoRepository
): RecursoFisicoService {

    override fun registrarRecursoFisico(request: RegistroRecursoFisicoRequest): RecursoFisicoResponse {
        val bloque = bloqueRepository.findByIdAndActivoTrue(request.bloqueId)
            ?: throw ResourceNotFoundException("No existe ese bloque.")

        if(recursoFisicoRepository.existsByNombreAndBloque(request.nombre, bloque)){
            throw BadRequestException("Ya existe un RF con ese nombre en este bloque.")
        }

        val recursoFisico = RecursoFisico(
            nombre = request.nombre,
            tipo = request.tipo,
            bloque = bloque
        )

        val rfGuardado = recursoFisicoRepository.save(recursoFisico)

        return rfGuardado.toResponse()
    }

    override fun listarRecursosFisicos(): List<RecursoFisicoResponse> {
        return recursoFisicoRepository.findAllByActivoTrue().map { it.toResponse() }
    }

    override fun obtenerRecursoFisicoPorId(id: Long): RecursoFisicoResponse {
        val recursoFisico = recursoFisicoRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Recurso físico no encontrado.")

        return recursoFisico.toResponse()
    }

    override fun actualizarRecursoFisico(id: Long, request: UpdateRecursoFisicoRequest): RecursoFisicoResponse {
        val recursoFisico = recursoFisicoRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Recurso físico no encontrado.")

        if(recursoFisico.nombre != request.nombre){
            val rfExistente = recursoFisicoRepository.findByNombreAndBloque(request.nombre, recursoFisico.bloque)

            if(rfExistente != null && rfExistente.id != recursoFisico.id){
                throw BadRequestException(
                    "Ya existe un recurso físico con ese nombre en este bloque."
                )
            }
            recursoFisico.nombre = request.nombre
        }

        return recursoFisicoRepository.save(recursoFisico).toResponse()
    }

    override fun eliminarRecursoFisico(id: Long) {
        val recursoFisico = recursoFisicoRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Recurso físico no encontrado.")

        recursoFisico.activo = false

        recursoFisicoRepository.save(recursoFisico)
    }
}
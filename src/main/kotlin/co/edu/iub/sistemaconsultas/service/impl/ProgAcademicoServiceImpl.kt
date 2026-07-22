package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.ProgAcademicoResponse
import co.edu.iub.sistemaconsultas.dto.RegistroProgAcademicoRequest
import co.edu.iub.sistemaconsultas.dto.UpdateProgAcademicoRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.ProgramaAcademico
import co.edu.iub.sistemaconsultas.repository.ProgAcademicoRepository
import co.edu.iub.sistemaconsultas.service.ProgAcademicoService
import org.springframework.stereotype.Service

@Service
class ProgAcademicoServiceImpl (
    private val programaRepository: ProgAcademicoRepository
): ProgAcademicoService {

    override fun registrarPrograma(
        request: RegistroProgAcademicoRequest
    ): ProgAcademicoResponse {

        val programaExistente = programaRepository.findByNombre(request.nombre)

        if (programaExistente != null) {
            throw BadRequestException("Ya existe un programa académico con ese nombre.")
        }

        val programa = ProgramaAcademico(

            nombre = request.nombre,

            activo = true
        )

        val programaGuardado = programaRepository.save(programa)

        return programaGuardado.toResponse()
    }

    override fun listarProgramas(): List<ProgAcademicoResponse> {

        return programaRepository.findAllByActivoTrue().map { it.toResponse() }

    }

    override fun obtenerProgramaPorId(
        id: Long
    ): ProgAcademicoResponse {

        val programa = programaRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Programa académico no encontrado.")

        return programa.toResponse()

    }

    override fun actualizarPrograma(
        id: Long,
        request: UpdateProgAcademicoRequest
    ): ProgAcademicoResponse {

        val programa = programaRepository
            .findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Programa académico no encontrado.")

        if (programa.nombre != request.nombre) {

            val programaExistente = programaRepository.findByNombre(request.nombre)

            if (
                programaExistente != null && programaExistente.id != programa.id
            ) {
                throw BadRequestException("Ya existe un programa académico con ese nombre.")
            }

            programa.nombre = request.nombre
        }

        val programaActulizado = programaRepository.save(programa)

        return programaActulizado.toResponse()

    }

    override fun eliminarPrograma(id: Long) {

        val programa = programaRepository
            .findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Programa académico no encontrado.")

        programa.activo = false

        programaRepository.save(programa)

    }

}
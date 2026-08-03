package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.sede.RegistroSedeRequest
import co.edu.iub.sistemaconsultas.dto.sede.SedeResponse
import co.edu.iub.sistemaconsultas.dto.sede.UpdateSedeRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.Sede
import co.edu.iub.sistemaconsultas.repository.SedeRepository
import co.edu.iub.sistemaconsultas.service.SedeService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class SedeServiceImpl(
    private val sedeRepository: SedeRepository,
): SedeService {

    override fun registrarSede(request: RegistroSedeRequest): SedeResponse {
        if (sedeRepository.existsByNombre(request.nombre)){
            throw BadRequestException("Ya existe una sede con ese nombre.")
        }

        val sede = Sede(
            nombre = request.nombre
        )

        val sedeGuardada = sedeRepository.save(sede)

        return sedeGuardada.toResponse()
    }

    
    override fun listarSedes(): List<SedeResponse> {
        return sedeRepository.findByActivoTrue().map { it.toResponse() }
    }

    override fun obtenerSedePorId(id: Long): SedeResponse {
        val sede = sedeRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Sede no encontrada.")

        return sede.toResponse()
    }

    override fun actualizarSede(id: Long, request: UpdateSedeRequest): SedeResponse {
        val sede = sedeRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Sede no encontrada.")

        if(sede.nombre != request.nombre){
            val sedeExistente = sedeRepository.findByNombre(request.nombre)

            if(sedeExistente != null && sedeExistente.id != sede.id ) {
                throw BadRequestException(
                    "Ya existe una sede con ese nombre."
                )
            }
            sede.nombre = request.nombre
        }

        return sedeRepository.save(sede).toResponse()
    }

    override fun eliminarSede(id: Long) {
        val sede = sedeRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Sede no encontrada.")

        sede.activo = false

        sedeRepository.save(sede)
    }
}
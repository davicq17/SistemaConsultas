package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.bloque.BloqueResponse
import co.edu.iub.sistemaconsultas.dto.bloque.RegistroBloqueRequest
import co.edu.iub.sistemaconsultas.dto.bloque.UpdateBloqueRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.Bloque
import co.edu.iub.sistemaconsultas.repository.BloqueRepository
import co.edu.iub.sistemaconsultas.repository.SedeRepository
import co.edu.iub.sistemaconsultas.service.BloqueService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class BloqueServiceImpl(
    private val sedeRepository: SedeRepository,
    private val bloqueRepository: BloqueRepository
): BloqueService {

    override fun registrarBloque(request: RegistroBloqueRequest): BloqueResponse {
        val sede = sedeRepository.findByIdAndActivoTrue(request.sedeId)
            ?: throw ResourceNotFoundException("La sede no existe.")

        if(bloqueRepository.existsByNombreAndSede(request.nombre, sede)){
            throw BadRequestException("Ya existe un bloque con ese nombre en esta sede.")
        }

        val bloque = Bloque(
            nombre = request.nombre,
            sede = sede
        )

        val bloqueGuardado = bloqueRepository.save(bloque)

        return bloqueGuardado.toResponse()
    }

    override fun listarBloques(): List<BloqueResponse> {
        return bloqueRepository.findAllByActivoTrue().map { it.toResponse() }
    }

    override fun obtenerBloquePorId(id: Long): BloqueResponse {
        val bloque = bloqueRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Bloque no encontrado.")

        return bloque.toResponse()
    }

    override fun actualizarBloque(id: Long, request: UpdateBloqueRequest): BloqueResponse {
        val bloque = bloqueRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Bloque no encontrado.")

        if(bloque.nombre != request.nombre){
            val bloqueExistente = bloqueRepository.findByNombreAndSede(request.nombre, bloque.sede)

            if(bloqueExistente != null && bloqueExistente.id != bloque.id) {
                throw BadRequestException("Ya existe un bloque con ese nombre en esta sede.")
            }
            bloque.nombre = request.nombre
        }

        return bloqueRepository.save(bloque).toResponse()
    }

    override fun eliminarBloque(id: Long) {
        val bloque = bloqueRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Bloque no encontrado.")

        bloque.activo = false

        bloqueRepository.save(bloque)
    }
}
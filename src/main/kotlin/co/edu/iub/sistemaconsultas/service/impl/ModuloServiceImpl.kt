package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.ModuloResponse
import co.edu.iub.sistemaconsultas.dto.RegistroModuloRequest
import co.edu.iub.sistemaconsultas.dto.UpdateModuloRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.Modulo
import co.edu.iub.sistemaconsultas.repository.ModuloRepository
import co.edu.iub.sistemaconsultas.service.ModuloService
import org.springframework.stereotype.Service

@Service
class ModuloServiceImpl (
    private val moduloRepository: ModuloRepository
): ModuloService {

    override fun registrarModulo(
        request: RegistroModuloRequest
    ): ModuloResponse {

        val moduloExistente = moduloRepository.findByNombre(request.nombre)

        if(moduloExistente != null){
            throw BadRequestException("Ya existe un módulo con ese nombre")
        }

        val modulo = Modulo(
            nombre = request.nombre,
            descripcion = request.descripcion,
            activo = true
        )

        val moduloGuardado = moduloRepository.save(modulo)

        return moduloGuardado.toResponse()
    }

    override fun listarModulos(): List<ModuloResponse> {

        return moduloRepository.findAllByActivoTrue().map { it.toResponse() }

    }

    override fun obtenerModuloPorId(id: Long): ModuloResponse {
        val modulo = moduloRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Módulo no encontrado.")

        return modulo.toResponse()
    }

    override fun actualizarModulo(
        id: Long,
        request: UpdateModuloRequest
    ): ModuloResponse {

        val modulo = moduloRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Módulo no encontrado.")

        if(modulo.nombre != request.nombre){

            val moduloExistente = moduloRepository.findByNombre(request.nombre)

            if(moduloExistente != null && moduloExistente.id != modulo.id){
                throw BadRequestException(
                    "Ya existe un módulo con ese nombre."
                )
            }
        }

        modulo.apply {
            nombre = request.nombre
            descripcion = request.descripcion
        }

        val moduloActulizado = moduloRepository.save(modulo)
        return moduloActulizado.toResponse()
    }

    override fun eliminarModulo(id: Long) {

        val modulo = moduloRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Módulo no encontrado.")

        modulo.activo = false

        moduloRepository.save(modulo)

    }
}
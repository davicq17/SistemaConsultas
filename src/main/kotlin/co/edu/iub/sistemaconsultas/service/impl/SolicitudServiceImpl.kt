package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.solicitud.RegistroSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.SolicitudConsultaResponse
import co.edu.iub.sistemaconsultas.dto.solicitud.UpdateSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.SolicitudConsultaMapper
import co.edu.iub.sistemaconsultas.repository.ModuloRepository
import co.edu.iub.sistemaconsultas.repository.SolicitudConsultaRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.SolicitudService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SolicitudServiceImpl(
    private val solicitudRepository: SolicitudConsultaRepository,
    private val usuarioRepository: UsuarioRepository,
    private val moduloRepository: ModuloRepository,
    private val mapper: SolicitudConsultaMapper
) : SolicitudService {

    @Transactional
    override fun crear(request: RegistroSolicitudConsultaRequest): SolicitudConsultaResponse {
        val estudiante = usuarioRepository.findById(request.estudianteId)
            .orElseThrow { ResourceNotFoundException("Estudiante no encontrado con ID: ${request.estudianteId}") }

        val docente = request.docenteId?.let { id ->
            usuarioRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Docente no encontrado con ID: $id") }
        }

        val modulo = moduloRepository.findById(request.moduloId)
            .orElseThrow { ResourceNotFoundException("Módulo no encontrado con ID: ${request.moduloId}") }

        val entidad = mapper.toEntity(request, estudiante, docente, modulo)
        return mapper.toResponse(solicitudRepository.save(entidad))
    }

    @Transactional(readOnly = true)
    override fun obtenerPorId(id: Long): SolicitudConsultaResponse {
        val entidad = solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud no encontrada con ID: $id") }
        return mapper.toResponse(entidad)
    }

    @Transactional(readOnly = true)
    override fun listarTodas(): List<SolicitudConsultaResponse> {
        return solicitudRepository.findAll().map { mapper.toResponse(it) }
    }

    @Transactional
    override fun actualizar(id: Long, request: UpdateSolicitudConsultaRequest): SolicitudConsultaResponse {
        val entidad = solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud no encontrada con ID: $id") }

        request.asunto?.let { entidad.asunto = it }
        request.descripcion?.let { entidad.descripcion = it }
        request.prioridad?.let { entidad.prioridad = it }
        request.estado?.let { entidad.estado = it }

        request.docenteId?.let { docenteId ->
            val docente = usuarioRepository.findById(docenteId)
                .orElseThrow { ResourceNotFoundException("Docente no encontrado con ID: $docenteId") }
            entidad.docente = docente
        }

        return mapper.toResponse(solicitudRepository.save(entidad))
    }

    @Transactional
    override fun eliminar(id: Long) {
        if (!solicitudRepository.existsById(id)) {
            throw ResourceNotFoundException("Solicitud no encontrada con ID: $id")
        }
        solicitudRepository.deleteById(id)
    }
}
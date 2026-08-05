package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.solicitud.RegistroSolicitudConsultaRequest
import co.edu.iub.sistemaconsultas.dto.solicitud.SolicitudConsultaResponse
import co.edu.iub.sistemaconsultas.model.Modulo
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario
import org.springframework.stereotype.Component

@Component
class SolicitudConsultaMapper {

    fun toResponse(entity: SolicitudConsulta): SolicitudConsultaResponse {
        return SolicitudConsultaResponse(
            id = entity.id ?: throw IllegalStateException("El ID de la solicitud no puede ser nulo"),
            asunto = entity.asunto,
            descripcion = entity.descripcion,
            prioridad = entity.prioridad,
            estado = entity.estado,
            fechaCreacion = entity.fechaCreacion,
            estudianteId = entity.estudiante?.id ?: throw IllegalStateException("El ID del estudiante no puede ser nulo"),
            docenteId = entity.docente?.id,
            moduloId = entity.modulo?.id ?: throw IllegalStateException("El ID del módulo no puede ser nulo")
        )
    }

    fun toEntity(
        request: RegistroSolicitudConsultaRequest,
        estudiante: Usuario,
        docente: Usuario?,
        modulo: Modulo
    ): SolicitudConsulta {
        return SolicitudConsulta(
            asunto = request.asunto,
            descripcion = request.descripcion,
            prioridad = request.prioridad,
            estudiante = estudiante,
            docente = docente,
            modulo = modulo
        )
    }
}
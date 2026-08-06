package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.solicitud.SolicitudConsultaResponse
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta

fun SolicitudConsulta.toResponse(): SolicitudConsultaResponse {
        return SolicitudConsultaResponse(
            id = id!!,

            numeroConsulta = numeroConsulta,

            estado = estado,

            prioridad = prioridad,

            asunto = asunto,

            descripcion = descripcion,

            fechaCreacion = fechaCreacion,

            fechaConsulta = fechaConsulta,

            horaConsulta = horaConsulta,

            identificacionEstudiante = estudiante.identificacion,

            nombreCompletoEstudiante = "${estudiante.nombre} ${estudiante.apellido}",

            identificacionDocente = docente.identificacion,

            nombreCompletoDocente = "${docente.nombre} ${docente.apellido}",

            nombreModulo = modulo.nombre,

            nombreRecursoFisico = recursoFisico?.nombre ?:"Sin asignar"
        )
}



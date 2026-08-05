package co.edu.iub.sistemaconsultas.dto.solicitud

import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud
import co.edu.iub.sistemaconsultas.model.enums.PrioridadSolicitud
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class SolicitudConsultaResponse(
    val id: Long,

    val numeroConsulta: String,

    val estado: EstadoSolicitud,

    val prioridad: PrioridadSolicitud,

    val asunto: String,

    val descripcion: String,

    val fechaCreacion: LocalDateTime,

    val fechaConsulta: LocalDate,

    val horaConsulta: LocalTime,

    val identificacionEstudiante: String,

    val nombreCompletoEstudiante: String,

    val identificacionDocente: String,

    val nombreCompletoDocente: String,

    val nombreModulo: String,

    val nombreRecursoFisico: String
)
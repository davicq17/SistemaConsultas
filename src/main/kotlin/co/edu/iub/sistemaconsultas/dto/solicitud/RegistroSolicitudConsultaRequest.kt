package co.edu.iub.sistemaconsultas.dto.solicitud

import co.edu.iub.sistemaconsultas.model.ProgramaAcademico
import co.edu.iub.sistemaconsultas.model.enums.PrioridadSolicitud
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalTime

data class RegistroSolicitudConsultaRequest(

    val prioridad: PrioridadSolicitud,

    @field:NotBlank(message = "El asunto es obligatorio.")
    @field:Size(min = 1, max = 100, message = "El asunto debe tener entre 1 y 100 caracteres.")
    val asunto: String,

    @field:NotBlank(message = "La descripción no puede estar vacía")
    val descripcion: String,

    val fechaConsulta: LocalDate,

    val horaConsulta: LocalTime,

    @field:NotNull(message = "El ID del docente es obligatorio.")
    val docenteId: Long,

    @field:NotNull(message = "El ID del módulo es obligatorio.")
    val moduloId: Long
    
)
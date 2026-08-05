package co.edu.iub.sistemaconsultas.dto.solicitud

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegistroSolicitudConsultaRequest(
    @field:NotBlank(message = "El asunto es obligatorio")
    val asunto: String,

    @field:NotBlank(message = "La descripción no puede estar vacía")
    val descripcion: String,

    val prioridad: String? = null,

    @field:NotNull(message = "El ID del estudiante es obligatorio")
    val estudianteId: Long,

    val docenteId: Long? = null,

    @field:NotNull(message = "El ID del módulo es obligatorio")
    val moduloId: Long
)
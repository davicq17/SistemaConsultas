package co.edu.iub.sistemaconsultas.dto.comentario

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegistroComentarioRequest(

    @field:NotBlank(message = "El comentario debe tener contenido.")
    val contenido: String,

    @field:NotNull(message = "La solicitud de consultas es obligatoria.")
    val solicitudConsultaId: Long
)

package co.edu.iub.sistemaconsultas.dto.solicitud

import jakarta.validation.constraints.NotNull

data class AsignarRecursoFisicoRequest(

    @field:NotNull(message = "El ID del recurso es obligatorio.")
    val recursoFisicoId: Long,
)

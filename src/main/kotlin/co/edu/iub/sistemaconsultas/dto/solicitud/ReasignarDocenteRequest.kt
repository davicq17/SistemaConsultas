package co.edu.iub.sistemaconsultas.dto.solicitud

import jakarta.validation.constraints.NotNull

data class ReasignarDocenteRequest(

    @field:NotNull(message = "El ID del docente es obligatorio.")
    val docenteId: Long,

)

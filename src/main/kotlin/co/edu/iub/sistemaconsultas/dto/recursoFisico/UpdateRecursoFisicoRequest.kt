package co.edu.iub.sistemaconsultas.dto.recursoFisico

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateRecursoFisicoRequest(

    @field:NotBlank( message = "El nombre es obligatorio.")
    @field:Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres.")
    val nombre: String,
)

package co.edu.iub.sistemaconsultas.dto.bloque

import jakarta.validation.constraints.*

data class RegistroBloqueRequest(

    @field:NotBlank( message = "El nombre es obligatorio.")
    @field:Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres.")
    val nombre: String,

    val sedeId: Long
)

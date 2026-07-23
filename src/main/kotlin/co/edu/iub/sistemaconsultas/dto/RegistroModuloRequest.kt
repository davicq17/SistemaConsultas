package co.edu.iub.sistemaconsultas.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegistroModuloRequest(

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    val nombre: String,

    @field:NotBlank(message = "La descripción es obligatoria")
    @field:Size(
        min = 10,
        max = 500,
        message = "La descripcion debe tener entre 10 y 500 caracteres"
    )
    val descripcion: String,
)

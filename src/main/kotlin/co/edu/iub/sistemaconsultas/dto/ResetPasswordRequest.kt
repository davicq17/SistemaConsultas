package co.edu.iub.sistemaconsultas.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ResetPasswordRequest(

    @field:NotBlank(message = "El token es obligatorio")
    val token: String,

    @field:NotBlank(message = "La nueva contraseña es obligatoria")
    @field:Size(
        min = 8,
        max = 100,
        message = "La contraseña debe tener entre 8 y 100 caracteres"
    )
    val nuevaPassword: String
)
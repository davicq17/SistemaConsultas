package co.edu.iub.sistemaconsultas.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ForgotPasswordRequest(

    @field:NotBlank(message = "El correo es obligatorio")
    @field:Email(message = "Debe ingresar un correo válido")
    val correo: String
)
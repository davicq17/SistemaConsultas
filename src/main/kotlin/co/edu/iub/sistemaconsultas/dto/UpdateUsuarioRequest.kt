package co.edu.iub.sistemaconsultas.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUsuarioRequest (

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    val nombre: String,

    @field:NotBlank(message = "El apellido es obligatorio")
    @field:Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    val apellido: String,

    @field:NotBlank(message = "El correo es obligatorio")
    @field:Email(message = "El correo no tiene un formato válido")
    val correo: String,

    val programaId: Long?,
)
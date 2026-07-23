package co.edu.iub.sistemaconsultas.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegistroUsuarioRequest(

    @field:NotBlank(message = "La identificación es obligatoria")
    @field:Size(max = 20, message = "La identificación no puede superar los 20 caracteres")
    val identificacion: String,

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    val nombre: String,

    @field:NotBlank(message = "El apellido es obligatorio")
    @field:Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    val apellido: String,

    @field:NotBlank(message = "El correo es obligatorio")
    @field:Email(message = "El correo no tiene un formato válido")
    val correo: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(
        min = 8,
        max = 100,
        message = "La contraseña debe tener entre 8 y 100 caracteres"
    )
    val password: String,

    @field:NotBlank(message = "El rol es obligatorio")
    val rol: String,

    val programaId: Long?
)

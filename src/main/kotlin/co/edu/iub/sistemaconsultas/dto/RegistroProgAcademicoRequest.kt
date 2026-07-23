package co.edu.iub.sistemaconsultas.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegistroProgAcademicoRequest(

    @field:NotBlank(message = "El nombre del programa es obligatorio")
    @field:Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres")
    val nombre: String
)

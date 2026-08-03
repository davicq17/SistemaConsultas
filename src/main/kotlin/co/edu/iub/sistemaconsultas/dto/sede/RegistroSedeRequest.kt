package co.edu.iub.sistemaconsultas.dto.sede

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegistroSedeRequest(

    @field:NotBlank(message= "el nombre es obligatorio.")
    @field:Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    val nombre: String
)

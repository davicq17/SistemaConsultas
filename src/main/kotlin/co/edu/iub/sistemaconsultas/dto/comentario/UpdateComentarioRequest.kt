package co.edu.iub.sistemaconsultas.dto.comentario

import jakarta.validation.constraints.NotBlank

data class UpdateComentarioRequest(

    @field:NotBlank(message = "El comentario debe tener contenido.")
    val contenido: String,
)

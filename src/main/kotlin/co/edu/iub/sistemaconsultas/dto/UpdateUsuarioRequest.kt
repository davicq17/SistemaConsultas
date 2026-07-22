package co.edu.iub.sistemaconsultas.dto

import co.edu.iub.sistemaconsultas.model.Rol
import java.time.LocalDateTime

data class UpdateUsuarioRequest (

    val nombre: String,

    val apellido: String,

    val correo: String,

    val programaId: Long?,
)
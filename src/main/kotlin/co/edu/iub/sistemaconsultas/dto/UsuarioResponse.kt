package co.edu.iub.sistemaconsultas.dto

import co.edu.iub.sistemaconsultas.model.Rol
import java.time.LocalDateTime

data class UsuarioResponse(

    val id: Long,

    val identificacion: String,

    val nombre: String,

    val apellido: String,

    val correo: String,

    val rol: Rol,

    val programaId: Long?,

    val programaNombre: String?,

    val activo: Boolean,

    val fechaCreacion: LocalDateTime

)

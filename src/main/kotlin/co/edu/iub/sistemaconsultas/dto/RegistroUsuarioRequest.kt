package co.edu.iub.sistemaconsultas.dto

data class RegistroUsuarioRequest(

    val identificacion: String,

    val nombre: String,

    val apellido: String,

    val correo: String,

    val password: String,

    val rol: String,

    val programaId: Long?
)

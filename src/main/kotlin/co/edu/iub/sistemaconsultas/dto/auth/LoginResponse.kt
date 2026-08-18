package co.edu.iub.sistemaconsultas.dto.auth

import co.edu.iub.sistemaconsultas.model.enums.Rol

data class LoginResponse(
    val token: String,
    val id_usuario: Long,
    val correo: String,
    val nombre: String,
    val rol: Rol,
    val tipo: String = "Bearer"
)
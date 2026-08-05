package co.edu.iub.sistemaconsultas.dto.auth

data class LoginResponse(

    val token: String,

    val tipo: String = "Bearer"
)
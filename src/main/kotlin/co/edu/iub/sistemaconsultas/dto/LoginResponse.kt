package co.edu.iub.sistemaconsultas.dto

data class LoginResponse(

    val token: String,

    val tipo: String = "Bearer"
)
package com.example.iubconsultas.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo")
    val email: String,

    val password: String
)

data class LoginResponse(
    val token: String,

    @SerializedName("id_usuario")
    val id: Long,

    @SerializedName("correo")
    val email: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("rol")
    val role: Role,

    val tipo: String = "Bearer"
)
package com.example.iubconsultas.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("identificacion")
    val identification: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("apellido")
    val lastName: String,

    @SerializedName("correo")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("rol")
    val role: Role,

    @SerializedName("programaId")
    val programId: Long? = null
)

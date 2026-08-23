package com.example.iubconsultas.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("nombre")
    val name: String,

    @SerializedName("apellido")
    val lastName: String,

    @SerializedName("correo")
    val email: String,

    @SerializedName("programaId")
    val programId: Long?
)

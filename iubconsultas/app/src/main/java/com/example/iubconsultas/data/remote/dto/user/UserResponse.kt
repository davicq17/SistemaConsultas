package com.example.iubconsultas.data.remote.dto.user

import com.example.iubconsultas.data.remote.dto.auth.Role
import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Long,

    @SerializedName("identificacion")
    val identification: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("apellido")
    val lastName: String,

    @SerializedName("correo")
    val email: String,

    @SerializedName("rol")
    val role: Role,

    @SerializedName("programaId")
    val programId: Long?,

    @SerializedName("programaNombre")
    val programName: String?,

    @SerializedName("activo")
    val active: Boolean,

    @SerializedName("fechaCreacion")
    val createdAt: String
)

fun UserResponse.displayName(): String = "$identification - $name $lastName"

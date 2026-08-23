package com.example.iubconsultas.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

enum class Role {
    @SerializedName("ADMINISTRADOR")
    ADMINISTRATOR,

    @SerializedName("DOCENTE")
    TEACHER,

    @SerializedName("ESTUDIANTE")
    STUDENT
}

fun Role.label(): String = when (this) {
    Role.ADMINISTRATOR -> "Administrador"
    Role.TEACHER -> "Docente"
    Role.STUDENT -> "Estudiante"
}

val REGISTRATION_ROLES = listOf(Role.STUDENT, Role.TEACHER)

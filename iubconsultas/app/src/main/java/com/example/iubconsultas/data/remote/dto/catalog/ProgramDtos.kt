package com.example.iubconsultas.data.remote.dto.catalog

import com.google.gson.annotations.SerializedName

data class ProgramResponse(
    val id: Long,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("activo")
    val active: Boolean
)

data class CreateProgramRequest(
    @SerializedName("nombre")
    val name: String
)

data class UpdateProgramRequest(
    @SerializedName("nombre")
    val name: String
)
package com.example.iubconsultas.data.remote.dto.catalog

import com.google.gson.annotations.SerializedName

data class SedeResponse(
    val id: Long,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("activo")
    val active: Boolean
)

data class CreateSedeRequest(
    @SerializedName("nombre")
    val name: String
)

data class UpdateSedeRequest(
    @SerializedName("nombre")
    val name: String
)
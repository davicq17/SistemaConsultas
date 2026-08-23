package com.example.iubconsultas.data.remote.dto.catalog

import com.google.gson.annotations.SerializedName

data class ModuleResponse(
    val id: Long,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("descripcion")
    val description: String,

    @SerializedName("activo")
    val active: Boolean
)

data class CreateModuleRequest(
    @SerializedName("nombre")
    val name: String,

    @SerializedName("descripcion")
    val description: String
)

data class UpdateModuleRequest(
    @SerializedName("nombre")
    val name: String,

    @SerializedName("descripcion")
    val description: String
)
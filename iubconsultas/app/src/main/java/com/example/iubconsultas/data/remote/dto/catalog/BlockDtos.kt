package com.example.iubconsultas.data.remote.dto.catalog

import com.google.gson.annotations.SerializedName

data class BlockResponse(
    val id: Long,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("sedeId")
    val sedeId: Long,

    @SerializedName("sedeNombre")
    val sedeName: String,

    @SerializedName("activo")
    val active: Boolean
)

data class CreateBlockRequest(
    @SerializedName("nombre")
    val name: String,

    @SerializedName("sedeId")
    val sedeId: Long
)

data class UpdateBlockRequest(
    @SerializedName("nombre")
    val name: String
)
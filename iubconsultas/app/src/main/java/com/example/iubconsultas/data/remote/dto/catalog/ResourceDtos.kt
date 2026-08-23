package com.example.iubconsultas.data.remote.dto.catalog

import com.google.gson.annotations.SerializedName

enum class ResourceType {
    @SerializedName("SALON")
    ROOM,

    @SerializedName("LABORATORIO")
    LABORATORY,

    @SerializedName("AUDITORIO")
    AUDITORIUM,

    @SerializedName("SALA")
    HALL,

    @SerializedName("OFICINA")
    OFFICE
}

fun ResourceType.label(): String = when (this) {
    ResourceType.ROOM -> "Salón"
    ResourceType.LABORATORY -> "Laboratorio"
    ResourceType.AUDITORIUM -> "Auditorio"
    ResourceType.HALL -> "Sala"
    ResourceType.OFFICE -> "Oficina"
}

fun resourceTypeFromLabel(label: String): ResourceType =
    ResourceType.entries.find { it.label() == label } ?: ResourceType.ROOM

data class ResourceResponse(
    val id: Long,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("bloqueId")
    val blockId: Long,

    @SerializedName("bloqueNombre")
    val blockName: String,

    @SerializedName("tipo")
    val type: ResourceType,

    @SerializedName("activo")
    val active: Boolean
)

data class CreateResourceRequest(
    @SerializedName("nombre")
    val name: String,

    @SerializedName("tipo")
    val type: ResourceType,

    @SerializedName("bloqueId")
    val blockId: Long
)

data class UpdateResourceRequest(
    @SerializedName("nombre")
    val name: String
)
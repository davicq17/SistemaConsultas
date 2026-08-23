package com.example.iubconsultas.data.remote.dto.comment

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    val id: Long,

    @SerializedName("contenido")
    val content: String,

    @SerializedName("autorId")
    val authorId: Long,

    @SerializedName("nombreAutor")
    val authorName: String,

    @SerializedName("consultaId")
    val consultationId: Long,

    @SerializedName("numeroConsulta")
    val consultationNumber: String,

    @SerializedName("fechaCreacion")
    val createdAt: String,

    @SerializedName("editado")
    val edited: Boolean
)

data class CreateCommentRequest(
    @SerializedName("contenido")
    val content: String,

    @SerializedName("solicitudConsultaId")
    val consultationId: Long
)

data class UpdateCommentRequest(
    @SerializedName("contenido")
    val content: String
)

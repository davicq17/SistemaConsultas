package co.edu.iub.sistemaconsultas.dto.comentario

import java.time.LocalDateTime

data class ComentarioResponse(

    val id: Long,

    val contenido: String,

    val autorId: Long,

    val nombreAutor: String,

    val consultaId: Long,

    val numeroConsulta: String,

    val fechaCreacion: LocalDateTime,

    val editado: Boolean
)

package co.edu.iub.sistemaconsultas.dto.bloque

data class BloqueResponse(

    val id: Long,

    val nombre: String,

    val sedeId: Long,

    val sedeNombre: String,

    val activo: Boolean
)

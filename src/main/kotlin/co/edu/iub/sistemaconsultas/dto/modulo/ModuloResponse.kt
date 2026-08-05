package co.edu.iub.sistemaconsultas.dto.modulo

data class ModuloResponse(

    val id: Long,

    val nombre: String,

    val descripcion: String,

    val activo: Boolean
)

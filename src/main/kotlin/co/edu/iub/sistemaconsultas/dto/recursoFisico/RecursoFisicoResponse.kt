package co.edu.iub.sistemaconsultas.dto.recursoFisico

import co.edu.iub.sistemaconsultas.model.enums.TipoRecursoFisico

data class RecursoFisicoResponse(

    val id: Long,

    val nombre: String,

    val bloqueId: Long,

    val bloqueNombre: String,

    val tipo: TipoRecursoFisico,

    val activo: Boolean
)

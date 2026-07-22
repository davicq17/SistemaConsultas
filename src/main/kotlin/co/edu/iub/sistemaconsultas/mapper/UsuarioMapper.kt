package co.edu.iub.sistemaconsultas.mapper

import co.edu.iub.sistemaconsultas.dto.UsuarioResponse
import co.edu.iub.sistemaconsultas.model.Usuario

fun Usuario.toResponse(): UsuarioResponse{

    return UsuarioResponse(

        id = id!!,

        identificacion = identificacion,

        nombre = nombre,

        apellido = apellido,

        correo = correo,

        rol = rol,

        programaId = programa?.id,

        programaNombre = programa?.nombre,

        activo = activo,

        fechaCreacion = fechaCreacion
    )
}

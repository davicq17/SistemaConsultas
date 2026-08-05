package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.usuario.UpdateUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.usuario.UsuarioResponse

interface UsuarioService {

    fun listarUsuarios(): List<UsuarioResponse>

    fun obtenerUsuarioPorId(id: Long): UsuarioResponse

    fun actualizarUsuario(
        id: Long,
        request: UpdateUsuarioRequest
    ): UsuarioResponse

    fun eliminarUsuario(id: Long)
}
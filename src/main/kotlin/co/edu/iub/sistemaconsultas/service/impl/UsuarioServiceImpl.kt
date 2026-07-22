package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.UpdateUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.UsuarioResponse
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.repository.ProgramaAcademicoRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.UsuarioService
import co.edu.iub.sistemaconsultas.mapper.toResponse
import org.springframework.stereotype.Service

@Service
class UsuarioServiceImpl(

    private val usuarioRepository: UsuarioRepository,
    private val programaAcademicoRepository: ProgramaAcademicoRepository

) : UsuarioService {

    override fun listarUsuarios(): List<UsuarioResponse> {

        return usuarioRepository.findAllByActivoTrue()
            .map { it.toResponse() }
    }

    override fun obtenerUsuarioPorId(id: Long): UsuarioResponse {
        val usuario = usuarioRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")

        return usuario.toResponse()
    }

    override fun actualizarUsuario(
        id: Long,
        request: UpdateUsuarioRequest
    ): UsuarioResponse {

        val usuario = usuarioRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")

        if (usuario.correo != request.correo) {
            val usuarioConCorreo = usuarioRepository.findByCorreo(request.correo)

            if (usuarioConCorreo != null && usuarioConCorreo.id != usuario.id){
                throw BadRequestException("El correo ya está registrado.")
            }
        }

        val programa = request.programaId?.let{programaId ->
            programaAcademicoRepository.findById(programaId)
                .orElseThrow {
                    BadRequestException("El programa académico no existe.")
                }
        }

        usuario.apply {
            nombre = request.nombre
            apellido = request.apellido
            correo = request.correo
            this.programa = programa
        }

        val usuarioActualizado = usuarioRepository.save(usuario)

        return usuarioActualizado.toResponse()
    }

    override fun eliminarUsuario(id: Long) {
        val usuario = usuarioRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")

        usuario.activo = false

        usuarioRepository.save(usuario)
    }
}
package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.comentario.ComentarioResponse
import co.edu.iub.sistemaconsultas.dto.comentario.RegistroComentarioRequest
import co.edu.iub.sistemaconsultas.dto.comentario.UpdateComentarioRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.Comentario
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud
import co.edu.iub.sistemaconsultas.repository.ComentarioRepository
import co.edu.iub.sistemaconsultas.repository.SolicitudConsultaRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.ComentarioService
import co.edu.iub.sistemaconsultas.service.NotificacionService
import co.edu.iub.sistemaconsultas.util.SecurityUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ComentarioServiceImpl(
    private val comentarioRepository: ComentarioRepository,
    private val usuarioRepository: UsuarioRepository,
    private val solicitudRepository: SolicitudConsultaRepository,
    private val notificacionService: NotificacionService
): ComentarioService {

    override fun registrarComentario(request: RegistroComentarioRequest): ComentarioResponse {
        val consulta = obtenerSolicitud(request.solicitudConsultaId)
        val autor = obtenerUsuarioAuth()
        validarParticipante(consulta, autor)

        if(!puedeComentar(consulta)){
            throw BadRequestException("No puede comentar esta consulta.")
        }

        val comentario = Comentario(
            contenido = request.contenido,
            fechaCreacion = LocalDateTime.now(),
            autor = autor,
            solicitudConsulta = consulta
        )

        val comentarioGuardado = comentarioRepository.save(comentario)
        val destinatario = obtenerDestinatario(consulta, autor)

        notificacionService.notificarComentario(comentarioGuardado, destinatario)
        return comentarioGuardado.toResponse()
    }

    override fun obtenerComentarioPorId(id: Long): ComentarioResponse {
        val comentario = obtenerComentario(id)
        val usuario = obtenerUsuarioAuth()
        validarParticipante(comentario.solicitudConsulta, usuario)
        return comentario.toResponse()
    }

    override fun actualizarComentario(id: Long, request: UpdateComentarioRequest): ComentarioResponse {
        val comentario = obtenerComentario(id)
        val autor = obtenerUsuarioAuth()
        validarAutor(comentario, autor)

        if(!puedeComentar(comentario.solicitudConsulta)){
            throw BadRequestException("No puede actualizar comentarios en esta consulta.")
        }
        comentario.apply {
            contenido = request.contenido
            editado = true
        }

        val comentarioGuardado = comentarioRepository.save(comentario)

        return comentarioGuardado.toResponse()
    }

    override fun listarComentariosPorSolicitud(solicitudId: Long): List<ComentarioResponse> {
        val consulta = obtenerSolicitud(solicitudId)
        val usuario = obtenerUsuarioAuth()
        validarParticipante(consulta, usuario)

        return comentarioRepository.findAllBySolicitudConsultaOrderByFechaCreacionAsc(consulta)
            .map { it.toResponse() }
    }

    private fun obtenerUsuarioAuth(): Usuario{
        val correo = SecurityUtils.obtenerCorreo()

        return usuarioRepository.findByCorreoAndActivoTrue(correo)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")
    }

    private fun obtenerSolicitud(id: Long): SolicitudConsulta {
        return  solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud no encontrada.") }
    }

    private fun obtenerComentario(id: Long): Comentario{
        return comentarioRepository.findById(id)
            .orElseThrow{ResourceNotFoundException("Comentario no encontrado.")}
    }

    private fun obtenerDestinatario(
        consulta: SolicitudConsulta,
        autor: Usuario
    ): Usuario {
        return if (autor.id == consulta.estudiante.id) {
            consulta.docente
        } else {
            consulta.estudiante
        }
    }

    private fun validarParticipante(
        consulta: SolicitudConsulta,
        usuario: Usuario
    ){
        if(usuario.id != consulta.estudiante.id &&
            usuario.id != consulta.docente.id){
            throw BadRequestException("El usuario no pertenece a esta solicitud.")
        }
    }

    private fun validarAutor(
        comentario: Comentario,
        usuario: Usuario
    ) {
        if (comentario.autor.id != usuario.id) {
            throw BadRequestException(
                "Solo puedes editar tus propios comentarios."
            )
        }
    }

    private fun puedeComentar(consulta: SolicitudConsulta): Boolean{
        return consulta.estado == EstadoSolicitud.ACEPTADA ||
                consulta.estado == EstadoSolicitud.EN_PROCESO
    }
}
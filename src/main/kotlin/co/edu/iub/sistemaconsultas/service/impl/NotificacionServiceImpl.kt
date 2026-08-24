package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.notificacion.NotificacionResponse
import co.edu.iub.sistemaconsultas.dto.notificacion.RegistroNotificacion
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.Comentario
import co.edu.iub.sistemaconsultas.model.Notificacion
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud
import co.edu.iub.sistemaconsultas.model.enums.TipoNotificacion
import co.edu.iub.sistemaconsultas.repository.NotificacionRepository
import co.edu.iub.sistemaconsultas.repository.SolicitudConsultaRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.EmailService
import co.edu.iub.sistemaconsultas.service.NotificacionService
import co.edu.iub.sistemaconsultas.util.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class NotificacionServiceImpl(
    private val notificacionRepository: NotificacionRepository,
    private val usuarioRepository: UsuarioRepository,
    private val solicitudRepository: SolicitudConsultaRepository,
    private val emailService: EmailService
):NotificacionService {

    private fun registrarNotificacion(
        request: RegistroNotificacion
    ): NotificacionResponse {
        val remitente = obtenerUsuario(request.remitenteId)
        val destinatario = obtenerUsuario(request.destinatarioId)
        val consulta = obtenerSolicitud(request.solicitudConsultaId)
        val notificacion = Notificacion(
            remitente = remitente,
            destinatario = destinatario,
            solicitudConsulta = consulta,
            tipo = request.tipo,
            titulo = request.titulo,
            leida = false,
            mensaje = request.mensaje
        )

        val notificacionGuardada = notificacionRepository.save(notificacion)
        if(notificacion.tipo != TipoNotificacion.COMENTARIO){
            emailService.enviarCorreo(
                destinatario.correo,
                notificacion.titulo,
                notificacion.mensaje
            )
        }
        return notificacionGuardada.toResponse()
    }

    override fun notificarNuevaSolicitud(solicitud: SolicitudConsulta) {
        armarNotificacion(
            solicitud.docente.id!!,
            TipoNotificacion.NUEVA_SOLICITUD,
            solicitud.id!!,
            "Nueva solicitud de consulta",
            """
                El estudiante ${solicitud.estudiante.nombre} ${solicitud.estudiante.apellido} ha creado una nueva solicitud de consulta con el número ${solicitud.numeroConsulta}.
                Revisa los detalles de la solicitud para gestionar su atención.
                """.trimIndent()
        )
    }

    override fun notificarCambioAgenda(solicitud: SolicitudConsulta, destinatario: Usuario, motivo: String) {
        armarNotificacion(
            destinatario.id!!,
            TipoNotificacion.CAMBIO_AGENDAMIENTO,
            solicitud.id!!,
            "Cambio en la agenda de tu consulta",
            """
                La consulta ${solicitud.numeroConsulta} ha sido reprogramada para el ${solicitud.fechaConsulta} a las ${solicitud.horaConsulta}. 
                Motivo: $motivo
                """.trimIndent()
        )
    }

    override fun notificarCambioEstado(solicitud: SolicitudConsulta, destinatario: Usuario, motivo: String?) {
        val (titulo, mensaje) = obtenerContenidoNotificacion(solicitud, motivo)
        val tipoNotificacion = obtenerTipoNotificacion(solicitud.estado)

        armarNotificacion(
            destinatario.id!!,
            tipoNotificacion,
            solicitud.id!!,
            titulo,
            mensaje
        )
    }

    override fun notificarAsignacionRecurso(solicitud: SolicitudConsulta, destinatario: Usuario) {
        armarNotificacion(
            destinatario.id!!,
            TipoNotificacion.ASIGNACION_RECURSO,
            solicitud.id!!,
            "Recurso físico asignado",
            """
                Se ha asignado el recurso ${solicitud.recursoFisico!!.nombre} para la consulta ${solicitud.numeroConsulta}.
                Consulta los detalles de tu solicitud para conocer la información actualizada.
                """.trimIndent()
        )
    }

    override fun notificarReasignacion(solicitud: SolicitudConsulta, nuevoDocente: Usuario) {
        armarNotificacion(
            nuevoDocente.id!!,
            TipoNotificacion.REASIGNACION,
            solicitud.id!!,
            "Reasignación de consulta",
            """
                Una nueva solicitud de consulta con el número ${solicitud.numeroConsulta} le ha sido asignada.
                Revisa los detalles de la solicitud para gestionar su atención.
                """.trimIndent()
        )
    }

    override fun notificarComentario(comentario: Comentario, destinatario: Usuario) {
        armarNotificacion(
            destinatario.id!!,
            TipoNotificacion.COMENTARIO,
            comentario.solicitudConsulta.id!!,
            "Nuevo comentario en la consulta ${comentario.solicitudConsulta.numeroConsulta}",
            """
                ${comentario.autor.nombre} ${comentario.autor.apellido} 
                ha agregado un nuevo comentario: ${comentario.contenido}
                """.trimIndent()
        )
    }

    override fun listarNotificaciones(): List<NotificacionResponse> {
        val destinatario = obtenerUsuarioAuth()
        return  notificacionRepository.findAllByDestinatarioAndTipoOrderByFechaCreacionDesc(
            destinatario,
            TipoNotificacion.COMENTARIO
        )
            .map { it.toResponse() }
    }

    override fun contarNotificacionesNoLeidas(): Long {
        val destinatario = obtenerUsuarioAuth()
        return  notificacionRepository.countByDestinatarioAndTipoAndLeidaFalse(
            destinatario,
            TipoNotificacion.COMENTARIO)
    }

    override fun marcarComoLeida(id: Long): NotificacionResponse {
        val destinatario = obtenerUsuarioAuth()
        val notificacion = notificacionRepository.findById(id)
            .orElseThrow{ ResourceNotFoundException("Notificación no encontrada.") }

        if (destinatario.id != notificacion.destinatario.id){
            throw BadRequestException("Esta notificación no le pertenece.")
        }

        notificacion.leida = true

        val notificacionGuarda = notificacionRepository.save(notificacion)

        return notificacionGuarda.toResponse()
    }

     private fun armarNotificacion(
        destinatarioId: Long,
        tipo: TipoNotificacion,
        solicitudId: Long,
        titulo: String,
        mensaje: String
    ){
        val remitente = obtenerUsuarioAuth()
        registrarNotificacion(
            RegistroNotificacion(
                remitenteId = remitente.id!!,
                destinatarioId = destinatarioId,
                solicitudConsultaId = solicitudId,
                tipo = tipo,
                titulo = titulo,
                mensaje = mensaje
            )
        )
    }

    private fun obtenerUsuarioAuth(): Usuario{
        val correo = SecurityUtils.obtenerCorreo()

        return usuarioRepository.findByCorreoAndActivoTrue(correo)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")
    }

    private fun obtenerUsuario(id: Long): Usuario{
        return usuarioRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")
    }

    private fun obtenerSolicitud(id: Long): SolicitudConsulta {
        return  solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud no encontrada.") }
    }

    private fun obtenerTipoNotificacion(
        estado: EstadoSolicitud
    ): TipoNotificacion{
        return when(estado){
            EstadoSolicitud.CANCELADA -> TipoNotificacion.CANCELACION
            else -> TipoNotificacion.CAMBIO_ESTADO
        }
    }

    private fun obtenerContenidoNotificacion(
        solicitud: SolicitudConsulta,
        motivo: String?
    ): Pair<String, String> {
        return when (solicitud.estado) {

            EstadoSolicitud.EN_PROCESO -> Pair(
                "Solicitud aceptada",
                """
                    Tu solicitud de consulta ${solicitud.numeroConsulta} ha sido aceptada y se encuentra actualmente en proceso de atención.
                    La consulta está programada para el ${solicitud.fechaConsulta} a las ${solicitud.horaConsulta}.
                    """.trimIndent()
            )

            EstadoSolicitud.RECHAZADA -> Pair(
                "Solicitud rechazada",
                """
                    Tu solicitud de consulta ${solicitud.numeroConsulta} ha sido rechazada.
                    Motivo: $motivo
                    """.trimIndent()
            )

            EstadoSolicitud.CANCELADA -> Pair(
                "Consulta cancelada",
                """
                    La consulta ${solicitud.numeroConsulta} ha sido cancelada. 
                    Motivo: $motivo
                    """.trimIndent()
            )

            EstadoSolicitud.RESUELTA -> Pair(
                "Consulta resuelta",
                "La consulta ${solicitud.numeroConsulta} ha sido marcada como resuelta."
            )

            else -> throw BadRequestException(
                "No existe una notificación definida para el estado ${solicitud.estado}."
            )
        }
    }
}
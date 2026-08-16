package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.eventoSolicitud.EventoSolicitudResponse
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.EventoSolicitud
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud
import co.edu.iub.sistemaconsultas.model.enums.TipoEvento
import co.edu.iub.sistemaconsultas.repository.EventoSolicitudRepository
import co.edu.iub.sistemaconsultas.repository.SolicitudConsultaRepository
import co.edu.iub.sistemaconsultas.service.EventoSolicitudService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class EventoSolicitudServiceImpl(
    private val eventoRepository: EventoSolicitudRepository,
    private val solicitudRepository: SolicitudConsultaRepository
): EventoSolicitudService {

    private fun registrarEvento(
        solicitud: SolicitudConsulta,
        usuario: Usuario,
        tipoEvento: TipoEvento,
        descripcion: String
    ){
        val evento = EventoSolicitud(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = tipoEvento,
            descripcion = descripcion,
            fechaEvento = LocalDateTime.now()
        )

        eventoRepository.save(evento)
    }

    override fun nuevaSolicitud(solicitud: SolicitudConsulta, usuario: Usuario) {
        registrarEvento(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = TipoEvento.CREACION,
            descripcion = """
                ${usuario.nombre} ${usuario.apellido} ha creado la solicitud de consulta ${solicitud.numeroConsulta}.
            """.trimIndent()
        )
    }

    override fun cambioEstado(solicitud: SolicitudConsulta, usuario: Usuario) {
        val tipoEvento = obtenerTipoEvento(solicitud.estado)
        val descripcion = obtenerContenidoDescripcion(solicitud, usuario)

        registrarEvento(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = tipoEvento,
            descripcion = descripcion
        )
    }

    override fun cambioAgenda(solicitud: SolicitudConsulta, usuario: Usuario) {
        registrarEvento(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = TipoEvento.CAMBIO_AGENDAMIENTO,
            descripcion = """
             ${usuario.nombre} ${usuario.apellido} reprogramó la consulta para el ${solicitud.fechaConsulta} a las ${solicitud.horaConsulta}.
            """.trimIndent()
        )
    }

    override fun asignacionRecurso(solicitud: SolicitudConsulta, usuario: Usuario) {
        val recurso = solicitud.recursoFisico
            ?: throw BadRequestException(
                "La solicitud no tiene un recurso físico asignado."
            )
        registrarEvento(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = TipoEvento.ASIGNACION_RECURSO,
            descripcion = """
                ${usuario.nombre} ${usuario.apellido} asignó el recurso ${recurso.nombre} a la consulta.
            """.trimIndent()
        )
    }

    override fun reasignacion(solicitud: SolicitudConsulta, usuario: Usuario) {
        registrarEvento(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = TipoEvento.REASIGNACION_DOCENTE,
            descripcion = """
                ${usuario.nombre} ${usuario.apellido} le reasignó la consulta al docente: 
                ${solicitud.docente.nombre} ${solicitud.docente.apellido}.
            """.trimIndent()
        )
    }

    override fun nuevoComentario(solicitud: SolicitudConsulta, usuario: Usuario) {
        registrarEvento(
            solicitud = solicitud,
            usuario = usuario,
            tipoEvento = TipoEvento.COMENTARIO,
            descripcion = """
                ${usuario.nombre} ${usuario.apellido} agregó un comentario a la consulta.
            """.trimIndent()
        )
    }

    override fun listarEventos(id: Long): List<EventoSolicitudResponse> {
        val solicitud = obtenerSolicitud(id)
        return eventoRepository.findAllBySolicitudOrderByFechaEventoDesc(
            solicitud
        ).map { it.toResponse() }
    }

    private fun obtenerSolicitud(id: Long): SolicitudConsulta {
        return  solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud no encontrada.") }
    }

    private fun obtenerTipoEvento(
        estado: EstadoSolicitud
    ): TipoEvento{
        return when(estado){
            EstadoSolicitud.RECHAZADA -> TipoEvento.RECHAZO
            EstadoSolicitud.CANCELADA -> TipoEvento.CANCELACION
            EstadoSolicitud.RESUELTA,
            EstadoSolicitud.EN_PROCESO ->  TipoEvento.CAMBIO_ESTADO
            else -> throw BadRequestException(
                "No existe evento definido para el estado $estado"
            )
        }
    }

    private fun obtenerContenidoDescripcion(
        solicitud: SolicitudConsulta,
        usuario: Usuario
    ): String{
        return when (solicitud.estado){

            EstadoSolicitud.EN_PROCESO -> """
                EL docente ${usuario.nombre} ${usuario.apellido} cambió el estado de la solicitud a EN_PROCESO..
            """.trimIndent()

            EstadoSolicitud.RESUELTA -> """
                El docente ${usuario.nombre} ${usuario.apellido} cambió el estado de la consulta a RESUELTA. 
            """.trimIndent()

            EstadoSolicitud.CANCELADA -> """
                ${usuario.nombre} ${usuario.apellido} ha cancelado la solicitud de consulta.
            """.trimIndent()

            EstadoSolicitud.RECHAZADA -> """
                ${usuario.nombre} ${usuario.apellido} ha rechazado la solicitud de consulta.
            """.trimIndent()

            else -> throw BadRequestException(
                "No existe evento definido para el estado ${solicitud.estado}"
            )
        }
    }
}
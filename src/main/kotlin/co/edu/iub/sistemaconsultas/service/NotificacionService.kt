package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.notificacion.NotificacionResponse
import co.edu.iub.sistemaconsultas.model.Comentario
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario

interface NotificacionService {

    fun notificarNuevaSolicitud(solicitud: SolicitudConsulta)

    fun notificarCambioEstado(solicitud: SolicitudConsulta, destinatario: Usuario, motivo: String? )

    fun notificarCambioAgenda(solicitud: SolicitudConsulta, destinatario: Usuario, motivo: String)

    fun notificarAsignacionRecurso(solicitud: SolicitudConsulta, destinatario: Usuario)

    fun notificarReasignacion(solicitud: SolicitudConsulta, nuevoDocente: Usuario)

    fun notificarComentario(comentario: Comentario, destinatario: Usuario)

    fun listarNotificaciones(): List<NotificacionResponse>

    fun contarNotificacionesNoLeidas(): Long

    fun marcarComoLeida(id: Long): NotificacionResponse

}
package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.notificacion.NotificacionResponse
import co.edu.iub.sistemaconsultas.dto.notificacion.RegistroNotificacionRequest
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.model.enums.TipoNotificacion

interface NotificacionService {

    fun notificarNuevaSolicitud(solicitud: SolicitudConsulta)

    fun notificarCambioEstado(solicitud: SolicitudConsulta, destinatario: Usuario, motivo: String? )

    fun notificarCambioAgenda(solicitud: SolicitudConsulta, destinatario: Usuario, motivo: String)

    fun notificarAsignacionRecurso(solicitud: SolicitudConsulta, destinatario: Usuario)

    fun notificarReasignacion(solicitud: SolicitudConsulta, nuevoDocente: Usuario)

}
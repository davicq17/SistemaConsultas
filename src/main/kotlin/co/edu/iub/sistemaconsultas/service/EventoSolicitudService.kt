package co.edu.iub.sistemaconsultas.service

import co.edu.iub.sistemaconsultas.dto.eventoSolicitud.EventoSolicitudResponse
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario

interface EventoSolicitudService {

    fun nuevaSolicitud(solicitud: SolicitudConsulta, usuario: Usuario)

    fun cambioEstado(solicitud: SolicitudConsulta, usuario: Usuario)

    fun cambioAgenda(solicitud: SolicitudConsulta, usuario: Usuario)

    fun asignacionRecurso(solicitud: SolicitudConsulta, usuario: Usuario)

    fun reasignacion(solicitud: SolicitudConsulta, usuario: Usuario)

    fun nuevoComentario(solicitud: SolicitudConsulta, usuario: Usuario)

    fun listarEventos(id: Long): List<EventoSolicitudResponse>
}
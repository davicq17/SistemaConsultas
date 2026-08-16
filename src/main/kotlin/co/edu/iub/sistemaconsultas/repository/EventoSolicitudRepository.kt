package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.EventoSolicitud
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import org.springframework.data.jpa.repository.JpaRepository

interface  EventoSolicitudRepository: JpaRepository<EventoSolicitud, Long> {

    fun findAllBySolicitudOrderByFechaEventoDesc(
        solicitud: SolicitudConsulta
    ):List<EventoSolicitud>
}
package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Comentario
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import org.springframework.data.jpa.repository.JpaRepository

interface ComentarioRepository: JpaRepository<Comentario, Long> {

    fun findAllBySolicitudConsultaOrderByFechaCreacionAsc(
        solicitudConsulta: SolicitudConsulta
    ): List<Comentario>
}
package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Notificacion
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.model.enums.TipoNotificacion
import org.springframework.data.jpa.repository.JpaRepository

interface NotificacionRepository: JpaRepository<Notificacion, Long> {
    fun findAllByDestinatarioAndTipoOrderByFechaCreacionDesc(
        destinatario: Usuario,
        tipo: TipoNotificacion
    ): List<Notificacion>

    fun countByDestinatarioAndTipoAndLeidaFalse(
        destinatario: Usuario,
        tipo: TipoNotificacion
    ): Long
}
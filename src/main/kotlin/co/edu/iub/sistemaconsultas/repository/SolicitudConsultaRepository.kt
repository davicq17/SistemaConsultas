package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SolicitudConsultaRepository :JpaRepository<SolicitudConsulta, Long>{

        fun findByNumeroConsulta(numeroConsulta: String): SolicitudConsulta?

        @Query("SELECT COALESCE(MAX(s.id), 0) FROM SolicitudConsulta s")
        fun obtenerUltimoIdRegistrado(): Long
}
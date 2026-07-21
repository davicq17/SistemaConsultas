package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import org.springframework.data.jpa.repository.JpaRepository

interface SolicitudConsultaRepository :
        JpaRepository<SolicitudConsulta, Long>
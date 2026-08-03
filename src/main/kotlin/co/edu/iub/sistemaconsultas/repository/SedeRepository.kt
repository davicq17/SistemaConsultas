package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Sede
import org.springframework.data.jpa.repository.JpaRepository

interface SedeRepository: JpaRepository<Sede, Long> {

    fun existsByNombre(nombre: String): Boolean

    fun findByNombre(nombre: String): Sede?

    fun findByActivoTrue(): List<Sede>

    fun findByIdAndActivoTrue(id: Long): Sede?
}
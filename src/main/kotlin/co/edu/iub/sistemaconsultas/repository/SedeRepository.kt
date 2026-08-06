package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Sede
import org.springframework.data.jpa.repository.JpaRepository

interface SedeRepository: JpaRepository<Sede, Long> {

    fun existsByNombreAndActivoTrue(nombre: String): Boolean

    fun findByNombreAndActivoTrue(nombre: String): Sede?

    fun findAllByActivoTrue(): List<Sede>

    fun findByIdAndActivoTrue(id: Long): Sede?
}
package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Bloque
import co.edu.iub.sistemaconsultas.model.Sede
import org.springframework.data.jpa.repository.JpaRepository

interface BloqueRepository: JpaRepository<Bloque, Long> {

    fun existsByNombreAndSedeAndActivoTrue(nombre: String, sede: Sede): Boolean

    fun findByNombreAndSedeAndActivoTrue(nombre: String, sede: Sede): Bloque?

    fun findAllByActivoTrue(): List<Bloque>

    fun findByIdAndActivoTrue(id: Long): Bloque?

    fun findBySedeAndActivoTrue(sede: Sede): List<Bloque>
}
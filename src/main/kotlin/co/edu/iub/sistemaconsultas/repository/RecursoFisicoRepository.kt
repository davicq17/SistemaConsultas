package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Bloque
import co.edu.iub.sistemaconsultas.model.RecursoFisico
import org.springframework.data.jpa.repository.JpaRepository

interface RecursoFisicoRepository: JpaRepository<RecursoFisico, Long> {

    fun existsByNombreAndBloqueAndActivoTrue(nombre: String, bloque: Bloque): Boolean

    fun findByNombreAndBloqueAndActivoTrue(nombre: String, bloque: Bloque): RecursoFisico?

    fun findAllByActivoTrue(): List<RecursoFisico>

    fun findByIdAndActivoTrue(id: Long): RecursoFisico?
}
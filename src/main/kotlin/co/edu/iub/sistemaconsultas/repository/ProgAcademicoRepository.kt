package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.ProgramaAcademico
import org.springframework.data.jpa.repository.JpaRepository

interface ProgAcademicoRepository :
    JpaRepository<ProgramaAcademico, Long>{

    fun findAllByActivoTrue(): List<ProgramaAcademico>

    fun findByIdAndActivoTrue(id: Long): ProgramaAcademico?

    fun findByNombre(nombre: String): ProgramaAcademico?
}
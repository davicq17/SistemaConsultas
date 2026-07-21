package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.ProgramaAcademico
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramaAcademicoRepository :
    JpaRepository<ProgramaAcademico, Long>
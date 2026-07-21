package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Modulo
import org.springframework.data.jpa.repository.JpaRepository

interface ModuloRepository:
        JpaRepository<Modulo, Long>
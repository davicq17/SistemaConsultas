package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long>{

    fun findByCorreoAndActivoTrue(correo: String): Usuario?

    fun existsByCorreoAndActivoTrue(correo: String): Boolean

    fun existsByIdentificacionAndActivoTrue(identificacion: String): Boolean

    fun findAllByActivoTrue(): List<Usuario>

    fun findByIdAndActivoTrue(id: Long): Usuario?
}
package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long>{

    fun findByCorreo(correo: String): Usuario?

    fun existsByCorreo(correo: String): Boolean

    fun existsByIdentificacion(identificacion: String): Boolean

    fun findAllByActivoTrue(): List<Usuario>

    fun findByIdAndActivoTrue(id: Long): Usuario?
}
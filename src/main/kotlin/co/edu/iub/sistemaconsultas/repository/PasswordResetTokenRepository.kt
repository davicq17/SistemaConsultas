package co.edu.iub.sistemaconsultas.repository

import co.edu.iub.sistemaconsultas.model.PasswordResetToken
import co.edu.iub.sistemaconsultas.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface PasswordResetTokenRepository:
    JpaRepository<PasswordResetToken, Long>{

    fun findByToken(token: String): PasswordResetToken?

    fun deleteByUsuario(usuario: Usuario)
}
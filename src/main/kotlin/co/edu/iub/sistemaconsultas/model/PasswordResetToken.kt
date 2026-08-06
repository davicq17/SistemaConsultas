package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "password_reset_tokens")
class PasswordResetToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    var usuario: Usuario,

    @Column(nullable = false)
    var fechaExpiracion: LocalDateTime,

    @Column(nullable = false)
    var utilizado: Boolean = false
)
package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuarios")
class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 20)
    var identificacion: String = "",

    @Column(nullable = false, length = 100)
    var nombre: String = "",

    @Column(nullable = false, length = 100)
    var apellido: String = "",

    @Column(nullable = false, unique = true, length = 150)
    var correo: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rol: Rol = Rol.ESTUDIANTE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id")
    var programa: ProgramaAcademico? = null,

    @Column(nullable = false)
    var activo: Boolean = true,

    @Column(nullable = false)
    var fechaCreacion: LocalDateTime = LocalDateTime.now()

)
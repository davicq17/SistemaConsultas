package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.Column
import jakarta.persistence.*
import jakarta.persistence.GenerationType
import java.time.LocalDateTime

@Entity
@Table(name = "comentarios")
class Comentario (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false,columnDefinition = "TEXT")
    var contenido: String = "",

    @Column(nullable = false)
    var fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column
    var editado: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    var autor: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    var solicitudConsulta: SolicitudConsulta
)
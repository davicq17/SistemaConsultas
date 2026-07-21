package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "solicitudes_consultas")
class SolicitudConsulta(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 200)
    var asunto: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var descripcion: String = "",

    @Column(length = 50)
    var prioridad: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE,

    @Column(nullable = false)
    var fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    var estudiante: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id")
    var docente: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    var modulo: Modulo? = null

)
package co.edu.iub.sistemaconsultas.model

import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud
import co.edu.iub.sistemaconsultas.model.enums.PrioridadSolicitud
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "solicitudes_consultas")
class SolicitudConsulta(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 30)
    var numeroConsulta: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var prioridad: PrioridadSolicitud = PrioridadSolicitud.MEDIA,

    @Column(nullable = false, length = 200)
    var asunto: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var descripcion: String = "",

    @Column(nullable = false)
    var fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var fechaConsulta: LocalDate,

    @Column(nullable = false)
    var horaConsulta: LocalTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    var estudiante: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    var docente: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    var modulo: Modulo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_fisico_id")
    var recursoFisico: RecursoFisico? = null
)
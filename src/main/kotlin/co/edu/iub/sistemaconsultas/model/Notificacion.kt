package co.edu.iub.sistemaconsultas.model
import co.edu.iub.sistemaconsultas.model.enums.TipoNotificacion
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notificaciones")
class Notificacion (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    var remitente: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    var destinatario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_consulta_id", nullable = false)
    var solicitudConsulta: SolicitudConsulta,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoNotificacion,

    @Column(nullable = false)
    var titulo: String = "",

    @Column(nullable = false)
    var mensaje: String = "",

    @Column(nullable = false)
    var fechaCreacion: LocalDateTime = LocalDateTime.now()
)
package co.edu.iub.sistemaconsultas.model

import co.edu.iub.sistemaconsultas.model.enums.TipoEvento
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name ="eventos_solicitud")
class EventoSolicitud (

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    var solicitud: SolicitudConsulta,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    var usuario: Usuario,

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    var tipoEvento: TipoEvento,

    @Column(nullable = false)
    var descripcion: String = "",

    @Column(nullable = false)
    var fechaEvento: LocalDateTime = LocalDateTime.now(),
)